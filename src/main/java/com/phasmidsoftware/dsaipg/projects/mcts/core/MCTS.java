package com.phasmidsoftware.dsaipg.projects.mcts.core;


/**
 * A generic interface for Monte Carlo Tree Search (MCTS) applicable to any game type T.
 * Defines the main phases of the MCTS algorithm.
 *
 * @param <T> the game type, which must implement the Game<T> interface
 */
public interface MCTS<T extends Game<T>> {

    /**
     * The main MCTS loop: runs selection, expansion, simulation, and backpropagation
     * for a given number of iterations starting from the root node.
     *
     * @param iterations the number of simulations to run
     * @return the best child node chosen after all simulations
     */
    Node<T> runSearch(int iterations);

    /**
     * Selection phase: recursively select the most promising node from the root
     * based on UCT or other tree policy, until a leaf or expandable node is found.
     *
     * @param node the node to start selecting from
     * @return the selected node for expansion
     */
    Node<T> select(Node<T> node);

    /**
     * Expansion phase: create one or more child nodes of the selected node,
     * based on the untried moves from the current game state.
     *
     * @param node the selected node to expand
     * @return one of the newly created children, selected for simulation
     */
    Node<T> expand(Node<T> node);

    /**
     * Simulation phase: from the given state, simulate until the terminal state is reached.
     *
     * @param state the starting state for simulation
     * @return the result of the simulation (e.g. winner ID or score)
     */
    int simulate(State<T> state);

    /**
     * Backpropagation phase: update statistics (e.g. wins, playouts) from the simulated result
     * along the path from the leaf node back to the root.
     *
     * @param node the node where simulation ended
     * @param result the outcome of the simulation used to update nodes
     */
    void backpropagate(Node<T> node, int result);
}

