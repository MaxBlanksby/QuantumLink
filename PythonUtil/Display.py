import networkx as nx
import matplotlib.pyplot as plt


G = nx.Graph()
# add sample nodes and edges so the graph is not empty
G.add_nodes_from([1, 2, 3,4])
G.add_edges_from([(1, 2), (2, 3), (3, 1)])

def draw_graph(graph):
    pos = nx.spring_layout(graph)
    nx.draw(graph, pos, with_labels=True)
    plt.show()

if __name__ == "__main__":
    draw_graph(G)
