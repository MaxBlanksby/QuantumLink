package Main;

import java.util.*;
import Layout.Layout;

public class Transpiler {

    private static final double LOOKAHEAD_WEIGHT = 0.5;
    private static final int NUM_ITERATIONS = 3;

    // ──────────────────────────────────────────────
    // Inner Data Structures
    // ──────────────────────────────────────────────

    private static class LogicalGate {
        String name;
        int[] qubits;

        LogicalGate(String name, int[] qubits) {
            this.name = name;
            this.qubits = qubits;
        }

        boolean isTwoQubit() {
            return qubits.length == 2;
        }
    }

    private static class QubitMapping {
        int[] logToPhys;
        int[] physToLog;
        int size;

        QubitMapping(int numQubits) {
            size = numQubits;
            logToPhys = new int[numQubits];
            physToLog = new int[numQubits];
            for (int i = 0; i < numQubits; i++) {
                logToPhys[i] = i;
                physToLog[i] = i;
            }
        }

        int l2p(int logical) {
            return logToPhys[logical];
        }

        void applySwap(int physA, int physB) {
            int logA = physToLog[physA];
            int logB = physToLog[physB];
            logToPhys[logA] = physB;
            logToPhys[logB] = physA;
            physToLog[physA] = logB;
            physToLog[physB] = logA;
        }

        QubitMapping copy() {
            QubitMapping m = new QubitMapping(size);
            System.arraycopy(logToPhys, 0, m.logToPhys, 0, size);
            System.arraycopy(physToLog, 0, m.physToLog, 0, size);
            return m;
        }
    }

    // ──────────────────────────────────────────────
    // Public API
    // ──────────────────────────────────────────────

    public Circuit route(Circuit circuit, Layout layout) {
        int numQubits = circuit.numQubits;
        List<LogicalGate> gates = extractGates(circuit);

        boolean hasTwoQubit = false;
        for (LogicalGate g : gates) {
            if (g.isTwoQubit()) { hasTwoQubit = true; break; }
        }
        if (!hasTwoQubit) {
            System.out.println("No two-qubit gates found. No routing needed.");
            return circuit;
        }

        List<int[]> edges = layout.generateCouplingEdges(numQubits);
        boolean[][] adjacent = buildAdjacencyMatrix(edges, numQubits);
        int[][] dist = computeDistances(adjacent, numQubits);

        List<LogicalGate> bestResult = null;
        int bestSwapCount = Integer.MAX_VALUE;
        QubitMapping currentMapping = new QubitMapping(numQubits);

        for (int iter = 0; iter < NUM_ITERATIONS; iter++) {
            // Forward pass
            QubitMapping fwdMapping = currentMapping.copy();
            List<LogicalGate> fwdResult = sabrePass(gates, adjacent, dist, fwdMapping, numQubits);

            int swapCount = countSwaps(fwdResult);
            if (swapCount < bestSwapCount) {
                bestSwapCount = swapCount;
                bestResult = fwdResult;
            }

            // Backward pass (reverse DAG, use forward's final mapping)
            List<LogicalGate> reversedGates = reverseGates(gates);
            QubitMapping bwdMapping = fwdMapping.copy();
            sabrePass(reversedGates, adjacent, dist, bwdMapping, numQubits);

            // Backward's final mapping becomes next iteration's initial mapping
            currentMapping = bwdMapping;
        }

        System.out.println("SABRE routing complete. SWAPs inserted: " + bestSwapCount);
        return buildOutputCircuit(bestResult, numQubits, circuit.circuitId);
    }

    // ──────────────────────────────────────────────
    // Gate Extraction from Circuit
    // ──────────────────────────────────────────────

    private List<LogicalGate> extractGates(Circuit circuit) {
        List<LogicalGate> gates = new ArrayList<>();

        for (Column col : circuit.columns) {
            Set<Integer> processedRows = new HashSet<>();

            // Handle two-qubit gates (control-target pairs)
            if (!col.controlBitsDepth.isEmpty()) {
                for (int controlDepth : col.controlBitsDepth) {
                    for (Cell cell : col.getCells()) {
                        if (col.controlBitsDepth.contains(cell.depthy)) continue;
                        if (cell.getGate().getGateType().equals("1")) continue;

                        gates.add(new LogicalGate(
                            cell.getGate().getGateType(),
                            new int[]{controlDepth, cell.depthy}
                        ));
                        processedRows.add(controlDepth);
                        processedRows.add(cell.depthy);
                    }
                }
            }

            // Handle single-qubit gates
            for (Cell cell : col.getCells()) {
                if (processedRows.contains(cell.depthy)) continue;
                if (cell.getGate().getGateType().equals("1")) continue;

                gates.add(new LogicalGate(
                    cell.getGate().getGateType(),
                    new int[]{cell.depthy}
                ));
            }
        }

        return gates;
    }

    // ──────────────────────────────────────────────
    // DAG Dependencies & Front Layer
    // ──────────────────────────────────────────────

    private int[][] buildDependencies(List<LogicalGate> gates) {
        int n = gates.size();
        List<List<Integer>> preds = new ArrayList<>();
        for (int i = 0; i < n; i++) preds.add(new ArrayList<>());

        Map<Integer, Integer> lastGateOnQubit = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int q : gates.get(i).qubits) {
                if (lastGateOnQubit.containsKey(q)) {
                    preds.get(i).add(lastGateOnQubit.get(q));
                }
                lastGateOnQubit.put(q, i);
            }
        }

        int[][] result = new int[n][];
        for (int i = 0; i < n; i++) {
            List<Integer> p = preds.get(i);
            result[i] = new int[p.size()];
            for (int j = 0; j < p.size(); j++) result[i][j] = p.get(j);
        }
        return result;
    }

    private int[][] buildSuccessors(List<LogicalGate> gates, int[][] predecessors) {
        int n = gates.size();
        List<List<Integer>> succs = new ArrayList<>();
        for (int i = 0; i < n; i++) succs.add(new ArrayList<>());

        for (int i = 0; i < n; i++) {
            for (int p : predecessors[i]) {
                succs.get(p).add(i);
            }
        }

        int[][] result = new int[n][];
        for (int i = 0; i < n; i++) {
            List<Integer> s = succs.get(i);
            result[i] = new int[s.size()];
            for (int j = 0; j < s.size(); j++) result[i][j] = s.get(j);
        }
        return result;
    }

    private List<Integer> getFrontLayer(List<LogicalGate> gates, int[][] predecessors, Set<Integer> executed) {
        List<Integer> front = new ArrayList<>();
        for (int i = 0; i < gates.size(); i++) {
            if (executed.contains(i)) continue;
            if (!gates.get(i).isTwoQubit()) continue;

            boolean ready = true;
            for (int pred : predecessors[i]) {
                if (!executed.contains(pred)) { ready = false; break; }
            }
            if (ready) front.add(i);
        }
        return front;
    }

    private List<Integer> getNextLayer(List<LogicalGate> gates, int[][] predecessors,
                                       List<Integer> frontLayer, Set<Integer> executed) {
        // Hypothetically execute front layer + any ready single-qubit gates
        Set<Integer> tempExecuted = new HashSet<>(executed);
        tempExecuted.addAll(frontLayer);

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < gates.size(); i++) {
                if (tempExecuted.contains(i) || gates.get(i).isTwoQubit()) continue;
                boolean ready = true;
                for (int pred : predecessors[i]) {
                    if (!tempExecuted.contains(pred)) { ready = false; break; }
                }
                if (ready) { tempExecuted.add(i); changed = true; }
            }
        }

        // Find ready 2-qubit gates
        List<Integer> next = new ArrayList<>();
        for (int i = 0; i < gates.size(); i++) {
            if (tempExecuted.contains(i) || !gates.get(i).isTwoQubit()) continue;
            boolean ready = true;
            for (int pred : predecessors[i]) {
                if (!tempExecuted.contains(pred)) { ready = false; break; }
            }
            if (ready) next.add(i);
        }
        return next;
    }

    // ──────────────────────────────────────────────
    // Coupling Graph Utilities
    // ──────────────────────────────────────────────

    private boolean[][] buildAdjacencyMatrix(List<int[]> edges, int numQubits) {
        boolean[][] adj = new boolean[numQubits][numQubits];
        for (int[] e : edges) {
            if (e[0] < numQubits && e[1] < numQubits) {
                adj[e[0]][e[1]] = true;
                adj[e[1]][e[0]] = true;
            }
        }
        return adj;
    }

    private int[][] computeDistances(boolean[][] adjacent, int numQubits) {
        int[][] dist = new int[numQubits][numQubits];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);

        for (int src = 0; src < numQubits; src++) {
            dist[src][src] = 0;
            Queue<Integer> queue = new LinkedList<>();
            queue.add(src);

            while (!queue.isEmpty()) {
                int curr = queue.poll();
                for (int nbr = 0; nbr < numQubits; nbr++) {
                    if (adjacent[curr][nbr] && dist[src][nbr] == Integer.MAX_VALUE) {
                        dist[src][nbr] = dist[src][curr] + 1;
                        queue.add(nbr);
                    }
                }
            }
        }
        return dist;
    }

    // ──────────────────────────────────────────────
    // Heuristic (with lookahead)
    // ──────────────────────────────────────────────

    private double heuristic(List<Integer> front, List<Integer> nextLayer,
                             List<LogicalGate> gates, QubitMapping mapping, int[][] dist) {
        double score = 0;
        for (int gIdx : front) {
            LogicalGate g = gates.get(gIdx);
            score += dist[mapping.l2p(g.qubits[0])][mapping.l2p(g.qubits[1])];
        }

        double nextScore = 0;
        for (int gIdx : nextLayer) {
            LogicalGate g = gates.get(gIdx);
            nextScore += dist[mapping.l2p(g.qubits[0])][mapping.l2p(g.qubits[1])];
        }

        return score + LOOKAHEAD_WEIGHT * nextScore;
    }

    // ──────────────────────────────────────────────
    // Core SABRE Pass
    // ──────────────────────────────────────────────

    private List<LogicalGate> sabrePass(List<LogicalGate> gates, boolean[][] adjacent,
                                        int[][] dist, QubitMapping mapping, int numQubits) {
        int[][] predecessors = buildDependencies(gates);
        int[][] successors = buildSuccessors(gates, predecessors);

        Set<Integer> executed = new HashSet<>();
        List<LogicalGate> output = new ArrayList<>();

        // Eagerly execute ready single-qubit gates
        executeSingleQubitGates(gates, predecessors, executed, mapping, output);

        List<Integer> front = getFrontLayer(gates, predecessors, executed);

        while (!front.isEmpty()) {
            // Phase 1: execute gates that are already on adjacent physical qubits
            List<Integer> executable = new ArrayList<>();
            for (int gIdx : front) {
                LogicalGate g = gates.get(gIdx);
                int p0 = mapping.l2p(g.qubits[0]);
                int p1 = mapping.l2p(g.qubits[1]);
                if (adjacent[p0][p1]) {
                    executable.add(gIdx);
                }
            }

            if (!executable.isEmpty()) {
                for (int gIdx : executable) {
                    LogicalGate g = gates.get(gIdx);
                    int p0 = mapping.l2p(g.qubits[0]);
                    int p1 = mapping.l2p(g.qubits[1]);
                    output.add(new LogicalGate(g.name, new int[]{p0, p1}));
                    executed.add(gIdx);
                }
                executeSingleQubitGates(gates, predecessors, executed, mapping, output);
                front = getFrontLayer(gates, predecessors, executed);
                continue;
            }

            // Phase 2: no gate is directly executable — pick the best SWAP
            Set<Integer> frontPhys = new HashSet<>();
            for (int gIdx : front) {
                LogicalGate g = gates.get(gIdx);
                frontPhys.add(mapping.l2p(g.qubits[0]));
                frontPhys.add(mapping.l2p(g.qubits[1]));
            }

            // Candidate SWAPs: coupling edges touching a front-layer physical qubit
            List<int[]> candidateSwaps = new ArrayList<>();
            for (int p = 0; p < numQubits; p++) {
                for (int q = p + 1; q < numQubits; q++) {
                    if (adjacent[p][q] && (frontPhys.contains(p) || frontPhys.contains(q))) {
                        candidateSwaps.add(new int[]{p, q});
                    }
                }
            }

            List<Integer> nextLayer = getNextLayer(gates, predecessors, front, executed);

            int[] bestSwap = null;
            double bestScore = Double.MAX_VALUE;

            for (int[] swap : candidateSwaps) {
                QubitMapping test = mapping.copy();
                test.applySwap(swap[0], swap[1]);
                double score = heuristic(front, nextLayer, gates, test, dist);
                if (score < bestScore) {
                    bestScore = score;
                    bestSwap = swap;
                }
            }

            if (bestSwap != null) {
                output.add(new LogicalGate("SWAP", new int[]{bestSwap[0], bestSwap[1]}));
                mapping.applySwap(bestSwap[0], bestSwap[1]);
            } else {
                System.err.println("SABRE: No valid SWAP found. Check coupling graph connectivity.");
                break;
            }
        }

        return output;
    }

    private void executeSingleQubitGates(List<LogicalGate> gates, int[][] predecessors,
                                         Set<Integer> executed, QubitMapping mapping,
                                         List<LogicalGate> output) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < gates.size(); i++) {
                if (executed.contains(i)) continue;
                if (gates.get(i).isTwoQubit()) continue;

                boolean ready = true;
                for (int pred : predecessors[i]) {
                    if (!executed.contains(pred)) { ready = false; break; }
                }

                if (ready) {
                    LogicalGate g = gates.get(i);
                    int phys = mapping.l2p(g.qubits[0]);
                    output.add(new LogicalGate(g.name, new int[]{phys}));
                    executed.add(i);
                    changed = true;
                }
            }
        }
    }

    // ──────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────

    private List<LogicalGate> reverseGates(List<LogicalGate> gates) {
        List<LogicalGate> reversed = new ArrayList<>();
        for (int i = gates.size() - 1; i >= 0; i--) {
            LogicalGate g = gates.get(i);
            reversed.add(new LogicalGate(g.name, g.qubits.clone()));
        }
        return reversed;
    }

    private int countSwaps(List<LogicalGate> output) {
        int count = 0;
        for (LogicalGate g : output) {
            if (g.name.equals("SWAP")) count++;
        }
        return count;
    }

    // ──────────────────────────────────────────────
    // Output Circuit Construction
    // ──────────────────────────────────────────────

    private Circuit buildOutputCircuit(List<LogicalGate> routedGates, int numQubits, String circuitId) {
        // Expand SWAP gates into 3 CNOTs each
        List<LogicalGate> expanded = new ArrayList<>();
        for (LogicalGate g : routedGates) {
            if (g.name.equals("SWAP")) {
                expanded.add(new LogicalGate("X", new int[]{g.qubits[0], g.qubits[1]}));
                expanded.add(new LogicalGate("X", new int[]{g.qubits[1], g.qubits[0]}));
                expanded.add(new LogicalGate("X", new int[]{g.qubits[0], g.qubits[1]}));
            } else {
                expanded.add(g);
            }
        }

        Circuit output = new Circuit();
        output.numQubits = numQubits;
        output.circuitId = circuitId + "_routed";
        output.columns = new ArrayList<>();

        // Pack non-conflicting gates into the same column
        String[] currentCol = new String[numQubits];
        Arrays.fill(currentCol, "1");
        boolean[] qubitUsed = new boolean[numQubits];
        List<Integer> currentControls = new ArrayList<>();

        for (LogicalGate g : expanded) {
            boolean conflict = false;
            for (int q : g.qubits) {
                if (qubitUsed[q]) { conflict = true; break; }
            }

            if (conflict) {
                finalizeColumn(output, currentCol, currentControls, numQubits);
                Arrays.fill(currentCol, "1");
                Arrays.fill(qubitUsed, false);
                currentControls.clear();
            }

            if (g.isTwoQubit()) {
                currentCol[g.qubits[0]] = "•";
                currentCol[g.qubits[1]] = g.name;
                qubitUsed[g.qubits[0]] = true;
                qubitUsed[g.qubits[1]] = true;
                currentControls.add(g.qubits[0]);
            } else {
                currentCol[g.qubits[0]] = g.name;
                qubitUsed[g.qubits[0]] = true;
            }
        }

        // Finalize last column if it has gates
        boolean hasGates = false;
        for (boolean u : qubitUsed) { if (u) { hasGates = true; break; } }
        if (hasGates) {
            finalizeColumn(output, currentCol, currentControls, numQubits);
        }

        return output;
    }

    private void finalizeColumn(Circuit output, String[] colData, List<Integer> controls, int numQubits) {
        Column col = new Column();
        int colIdx = output.columns.size();
        for (int i = 0; i < numQubits; i++) {
            col.addCell(new Cell(new Gate(colData[i]), colIdx, i));
        }
        col.controlBitsDepth.addAll(controls);
        output.columns.add(col);
    }
}