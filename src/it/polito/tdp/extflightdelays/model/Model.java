package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	private Graph<String, DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private List<String> stati;
	
	
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
		stati = new LinkedList<>(dao.loadAllStates());
	}

	public void creaGrafo() {
		grafo = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.loadAllStates());
		for(String s: this.grafo.vertexSet()) {
			for(String s2: stati) {
				int peso = dao.getNumeroDiVoli(s, s2);
				Graphs.addEdge(grafo, s, s2, peso);
			}
			
		}
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
	}
	public List<String> getStati(){
		Collections.sort(stati);
		return stati;
	}
}
