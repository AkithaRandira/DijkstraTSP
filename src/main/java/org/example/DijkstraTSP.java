package org.example;

import java.util.ArrayList;
import java.util.List;

class DijkstraTSP {

    // Implement Dijkstra's algorithm to find shortest path
    public static void dijkstraAlgorithm(int[][] graph, int source, int[] dist) {
        int nodes = graph.length;
        boolean[] visitedVertex = new boolean[nodes];
        for (int i = 0; i < nodes; i++) {
            visitedVertex[i] = false;
            dist[i] = Integer.MAX_VALUE;
        }

        dist[source] = 0;
        for (int i = 0; i < nodes; i++) {
            int u = findMinDistance(dist, visitedVertex);
            visitedVertex[u] = true;

            for (int v = 0; v < nodes; v++) {
                if (!visitedVertex[v] && graph[u][v] != 0 && (dist[u] + graph[u][v] < dist[v])) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }
    }

    // Helper method to find the minimum distance
    private static int findMinDistance(int[] dist, boolean[] visitedVertex) {
        int minDistance = Integer.MAX_VALUE;
        int minDistanceVertex = -1;
        for (int i = 0; i < dist.length; i++) {
            if (!visitedVertex[i] && dist[i] < minDistance) {
                minDistance = dist[i];
                minDistanceVertex = i;
            }
        }
        return minDistanceVertex;
    }

    // Implement modified Dijkstra's algorithm for TSP
    public static int tspAlgorithm(int[][] distance, int cities) {
        boolean[] visitCity = new boolean[cities];
        visitCity[0] = true;

        return findHamiltonianCycle(distance, visitCity, 0, cities, 1, 0, Integer.MAX_VALUE);
    }

    // Helper method to find the minimum weighted Hamiltonian Cycle
    private static int findHamiltonianCycle(int[][] distance, boolean[] visitCity, int currPos, int cities, int count, int cost, int hamiltonianCycle) {
        if (count == cities && distance[currPos][0] > 0) {
            hamiltonianCycle = Math.min(hamiltonianCycle, cost + distance[currPos][0]);
            return hamiltonianCycle;
        }

        for (int i = 0; i < cities; i++) {
            if (!visitCity[i] && distance[currPos][i] > 0) {
                visitCity[i] = true;
                hamiltonianCycle = findHamiltonianCycle(distance, visitCity, i, cities, count + 1, cost + distance[currPos][i], hamiltonianCycle);
                visitCity[i] = false;
            }
        }
        return hamiltonianCycle;
    }

    // Helper method to return the shortest route
    public static String getShortestRoute(int[] dist, int cities, int[][] distanceMatrix, String[] cityNames) {
        int startCityIndex = 0; // Starting city index
        int minDistance = dist[startCityIndex];

        for (int i = 1; i < cities; i++) {
            if (dist[i] < minDistance) {
                minDistance = dist[i];
                startCityIndex = i;
            }
        }

        List<String> route = new ArrayList<>();
        int currentCityIndex = startCityIndex;
        boolean[] visited = new boolean[cities];
        visited[currentCityIndex] = true;

        route.add(cityNames[currentCityIndex]);

        for (int i = 1; i < cities; i++) {
            int nextCityIndex = findMinDistanceIndex(distanceMatrix[currentCityIndex], visited);
            route.add(cityNames[nextCityIndex]);
            visited[nextCityIndex] = true;
            currentCityIndex = nextCityIndex;
        }

        route.add(cityNames[startCityIndex]); // Return to the starting city

        return String.join(" -> ", route);
    }

    // Helper method to find the index of the minimum distance
    private static int findMinDistanceIndex(int[] distances, boolean[] visited) {
        int minIndex = -1;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < distances.length; i++) {
            if (!visited[i] && distances[i] < minDistance) {
                minDistance = distances[i];
                minIndex = i;
            }
        }


        if (minIndex == -1) {
            for (int i = 0; i < visited.length; i++) {
                if (!visited[i]) {
                    return i;
                }
            }
        }

        return minIndex;
    }

    public static void main(String[] args) {
        // Create and initialize the graph
        String[] cityNames = {"A", "B", "C", "D", "E"};
        int[][] distanceMatrix = {
                {0, 10, 15, 20, 30},
                {10, 0, 35, 25, 40},
                {15, 35, 0, 30, 50},
                {20, 25, 30, 0, 70},
                {30, 40, 50, 70, 0}
        };

        int cities = cityNames.length;
        int[] dist = new int[cities];

        // Call modified Dijkstra's algorithm
        dijkstraAlgorithm(distanceMatrix, 0, dist);

        // Call modified TSP algorithm
        int tspResult = tspAlgorithm(distanceMatrix, cities);

        // Print results
        System.out.println("Shortest route: " + getShortestRoute(dist, cities, distanceMatrix, cityNames));
        System.out.println("Total distance: " + tspResult + " units");
    }
}
