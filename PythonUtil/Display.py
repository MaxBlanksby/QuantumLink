import networkx as nx
import matplotlib.pyplot as plt
from pathlib import Path
import sys

# Add parent directory to path to import Util
sys.path.insert(0, str(Path(__file__).parent))
from Util import load_json


def load_graph_from_json(json_path):
    """Load graph data from JSON file and convert to NetworkX graph."""
    graph_data = load_json(json_path)
    
    if graph_data is None:
        print(f"Error: Could not load graph from {json_path}")
        return None
    
    G = nx.DiGraph()  # Use directed graph since we have source/target links
    
    # Add all nodes
    for node in graph_data:
        node_id = (node['colId'], node['rowId'])
        G.add_node(node_id, label=node['label'], col=node['colId'], row=node['rowId'])
    
    # Add edges from target links (outgoing connections)
    for node in graph_data:
        source_id = (node['colId'], node['rowId'])
        for link in node.get('targetLinks', []):
            target_id = (link['targetColId'], link['targetRowId'])
            G.add_edge(source_id, target_id)
    
    return G


def draw_graph(graph):
    """Draw the graph with custom layout and labels."""
    if graph is None or len(graph.nodes) == 0:
        print("Error: Graph is empty")
        return
    
    # Create position layout based on column and row
    pos = {}
    for node_id, data in graph.nodes(data=True):
        col = data['col']
        row = data['row']
        # Position: x = column, y = -row (negative to flip vertically)
        pos[node_id] = (col * 2, -row * 2)
    
    # Create labels showing column, row, and gate type
    labels = {node_id: f"{data['label']}\n({data['col']},{data['row']})" 
              for node_id, data in graph.nodes(data=True)}
    
    plt.figure(figsize=(14, 8))
    
    # Draw nodes
    nx.draw_networkx_nodes(graph, pos, node_color='lightblue', 
                          node_size=1500, alpha=0.9)
    
    # Draw edges with arrows
    nx.draw_networkx_edges(graph, pos, edge_color='gray', 
                          arrows=True, arrowsize=20, arrowstyle='->', 
                          connectionstyle='arc3,rad=0.1', width=2)
    
    # Draw labels
    nx.draw_networkx_labels(graph, pos, labels, font_size=10, font_weight='bold')
    
    plt.title("Quantum Circuit Graph Visualization", fontsize=16, fontweight='bold')
    plt.xlabel("Time Step (Column)", fontsize=12)
    plt.ylabel("Qubit (Row)", fontsize=12)
    plt.grid(True, alpha=0.3)
    plt.axis('on')
    plt.tight_layout()
    plt.show()


if __name__ == "__main__":
    # Load the graph from the DebugGraph.json file
    graph_path = Path(__file__).parent.parent / "Graphs" / "DebugGraph.json"
    print(f"Loading graph from: {graph_path}")
    
    G = load_graph_from_json(graph_path)
    
    if G:
        print(f"Graph loaded successfully!")
        print(f"  Nodes: {G.number_of_nodes()}")
        print(f"  Edges: {G.number_of_edges()}")
        draw_graph(G)
    else:
        print("Failed to load graph.")
