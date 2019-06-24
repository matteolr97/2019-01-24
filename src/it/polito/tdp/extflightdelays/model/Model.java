package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
		Graphs.addAllVertices(grafo, stati);
		for(String s: stati) {
			for(String s2: stati) {
				int peso = dao.getNumeroDiVoli(s, s2);
				DefaultWeightedEdge e = grafo.getEdge(s, s2);
				if(e==null)
				Graphs.addEdgeWithVertices(grafo, s, s2, peso);
			}
			
		}
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
	}
	public List<String> getStati(){
		Collections.sort(stati);
		return this.stati;
	}
	public String getVoli(String stato) {
		String res = "";
		if(this.grafo == null)
			return "Devi prima creare il grafo!";
		
		List<DefaultWeightedEdge> outgoing = new LinkedList<DefaultWeightedEdge>();
		outgoing.addAll(this.grafo.outgoingEdgesOf(stato));
		Collections.sort(outgoing, new Comparator<DefaultWeightedEdge>(){

			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
				return (int)grafo.getEdgeWeight(o2) - (int)grafo.getEdgeWeight(o1);
			}
			
		});
		
		for(DefaultWeightedEdge edge : outgoing){
			if(grafo.getEdgeTarget(edge) != stato && grafo.getEdgeWeight(edge) > 0){
				res += grafo.getEdgeTarget(edge);
				res += "\t";
				res += grafo.getEdgeWeight(edge);
				res += "\n";
			}
		}
		return res;
		
	}
	}
