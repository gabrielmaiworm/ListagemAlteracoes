package com.maiworm.alteracao.domain;

import com.maiworm.alteracao.domain.enumeration.Ala;
import com.maiworm.alteracao.domain.enumeration.Status;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Apartamento.
 */
@Entity
@Table(name = "apartamento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Apartamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private Integer numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "ala")
    private Ala ala;

    @Column(name = "responsavel")
    private String responsavel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "detalhes")
    private String detalhes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Apartamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Apartamento numero(Integer numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Ala getAla() {
        return this.ala;
    }

    public Apartamento ala(Ala ala) {
        this.setAla(ala);
        return this;
    }

    public void setAla(Ala ala) {
        this.ala = ala;
    }

    public String getResponsavel() {
        return this.responsavel;
    }

    public Apartamento responsavel(String responsavel) {
        this.setResponsavel(responsavel);
        return this;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Status getStatus() {
        return this.status;
    }

    public Apartamento status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDetalhes() {
        return this.detalhes;
    }

    public Apartamento detalhes(String detalhes) {
        this.setDetalhes(detalhes);
        return this;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Apartamento)) {
            return false;
        }
        return id != null && id.equals(((Apartamento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Apartamento{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", ala='" + getAla() + "'" +
            ", responsavel='" + getResponsavel() + "'" +
            ", status='" + getStatus() + "'" +
            ", detalhes='" + getDetalhes() + "'" +
            "}";
    }
}
