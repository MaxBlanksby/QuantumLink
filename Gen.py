import qiskit
from qiskit.circuit.random import random_circuit
import cirq
from qiskit import QuantumCircuit
import random




#cirq
# q = cirq.LineQubit.range(6)
# circuit = cirq.testing.random_circuit(q, n_moments=15, op_density=0.7)
# print(circuit)


#qiskit
#qc = random_circuit(10, 20, max_operands=2, seed=42)
#print(qc)




def genBasicCircuit(n_qubits: int, depth: int, p_ccx: float = 0.3, seed: int | None = None) -> QuantumCircuit:
    """
    Generate a random quantum circuit with only CX and CCX gates.
    
    Args:
        n_qubits: number of qubits
        depth: number of layers (each layer may have several gates)
        p_ccx: probability of placing a Toffoli instead of a CNOT
        seed: random seed for reproducibility
    
    Returns:
        QuantumCircuit with n_qubits qubits and given depth.
    """

    rng = random.Random(seed)
    qc = QuantumCircuit(n_qubits)

    for _ in range(depth):
        if rng.random() < p_ccx and n_qubits >= 3:
            # place a Toffoli (ccx)
            a, b, c = rng.sample(range(n_qubits), 3)
            qc.ccx(a, b, c)
        else:
            # place a CNOT (cx)
            ctrl, tgt = rng.sample(range(n_qubits), 2)
            qc.cx(ctrl, tgt)

    return qc



qc = genBasicCircuit(n_qubits=100, depth=100, p_ccx=0.4, seed=43)
print(qc)