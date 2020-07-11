package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private List<Integer> anni;
	
	//Grafo semplice, pesato, non orientato
	private Graph<Director, DefaultWeightedEdge> grafo;
	private List<Director> registi;
	private Map<Integer, Director> mappa;
	private List<Collegamento> collegamenti;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.anni = new ArrayList<>();
	}

	public List<Integer> elencoAnni(){
		for(int i= 2004; i<2007; i++) {
			this.anni.add(i);
		}
		return this.anni;
	}
	
	public void creaGrafo(int anno) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.registi = new ArrayList<>(this.dao.direttoriAnno(anno));
		
		this.mappa = new HashMap<>();
		
		for(Director d: registi) {
			mappa.put(d.getId(), d);
		}
		
		//VERTICI
		Graphs.addAllVertices(this.grafo, this.registi);
		
		this.collegamenti = new ArrayList<>(this.dao.prendiCollegamenti(anno, mappa));
		
		//ARCHI
		for(Collegamento c: this.collegamenti) {
			Graphs.addEdge(this.grafo, c.getD1(), c.getD2(), c.getPeso());
		}
		
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Director> elencoRegisti(){
		this.registi = new ArrayList<>(this.grafo.vertexSet());
		return this.registi;
	}
	
	public String registiAdiacenti(Director scelto) {
		
		List<Collegamento> ordinata = new ArrayList<>();
		
		for(Director di: Graphs.neighborListOf(this.grafo, scelto)) {
			int peso = (int)this.grafo.getEdgeWeight(this.grafo.getEdge(scelto, di));
			ordinata.add(new Collegamento(scelto, di, peso));
		}
		
		Collections.sort(ordinata);
		
		String stampa = "";
		for(Collegamento c: ordinata) {
			stampa += c.getD2()+" - # attori condivisi: "+c.getPeso()+"\n";
		}
		return stampa;
	}
	
	//---PUNTO 2---
	private List<Director> soluzione;
	private int max;
	private int peso;
	
	public String cercaAffini(int c, Director scelto) {
		
		
		LinkedList<Director> parziale = new LinkedList<>();
		
		parziale.add(scelto);
		
		peso = 0;
		
		List<Director> disponibili = new ArrayList<>(this.registi);
		
		disponibili.remove(scelto);
		
		this.max = c;
		
		this.soluzione = new ArrayList<>(parziale);
		
		soluzione.remove(scelto);	//per non avere lo stesso regista nella lista dei registi

		ricorsione(parziale, peso, disponibili);
		
		String stampa = "";
		for(Director d: soluzione) {
			stampa += d.toString()+"\n";
		}
		return stampa+"Per un totale di "+peso+" attori condivisi.";
	}
	
	private void ricorsione(LinkedList<Director> parziale, int peso, List<Director> disponibili) {
		//caso finale--> max (attori condivisi)<=c e quado non ho più registi da considerare
		if(peso> this.max) {
			return;
		}
		if(parziale.size()>soluzione.size()) {
			this.soluzione = new ArrayList<>(parziale);
			this.peso = peso;
		}
		//caso intermedio
		Director ultimo = parziale.getLast();
		
		for(Director d: Graphs.neighborListOf(this.grafo, ultimo)) {
			if(disponibili.contains(d)) {
				parziale.add(d);
				
				List<Director> copia = new ArrayList<>(disponibili);
				copia.remove(d);
				
				int calcolo = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-2), d));
				
				ricorsione(parziale, peso+calcolo, copia);
				
				parziale.remove(d);
			}
		}
	}
}
