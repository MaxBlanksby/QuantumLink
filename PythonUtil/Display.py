import networkx as nx
import matplotlib.pyplot as plt




G = nx.Graph()

def draw_graph(G):
    pos = nx.spring_layout(G)
    nx.draw(G, pos, with_labels=True)
    plt.show()
