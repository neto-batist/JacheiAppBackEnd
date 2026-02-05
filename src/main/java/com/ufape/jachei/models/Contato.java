package com.ufape.jachei.models;

import jakarta.persistence.*;

@Entity
@Table(name = "contato")
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "telefone", length = 25)
    private String telefone;

    @Column(name = "celular", length = 25)
    private String celular;

    @Column(name = "whatsApp", nullable = false)
    private Byte whatsApp;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "instagramLink", length = 300)
    private String instagramLink;

    @Column(name = "faceBookLink", length = 300)
    private String faceBookLink;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Byte getWhatsApp() {
        return whatsApp;
    }

    public void setWhatsApp(Byte whatsApp) {
        this.whatsApp = whatsApp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getFaceBookLink() {
        return faceBookLink;
    }

    public void setFaceBookLink(String faceBookLink) {
        this.faceBookLink = faceBookLink;
    }

}