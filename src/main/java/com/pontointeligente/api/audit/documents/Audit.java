package com.pontointeligente.api.audit.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "audit")
public class Audit {

	
	@Id
	private String id;
	private String nomeSistema;
	private String ip;
	private String acao;
	
	public Audit() {
	}
	
	public Audit(String nomeSistema, String ip, String acao) {
	
		super();
		this.nomeSistema = nomeSistema;
		this.ip = ip;
		this.acao = acao;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNomeSistema() {
		return nomeSistema;
	}

	public void setNomeSistema(String nomeSistema) {
		this.nomeSistema = nomeSistema;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}
	
}
