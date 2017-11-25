/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author rafao
 */
public class MatriculaComponenteUfrnDTO {
    
    private Integer ano;
    private Boolean conceito;
    private Integer faltas;
    private Integer idComponente;
    private Integer idDiscente;
    private Integer idMatriculaComponente;
    private Integer idSituacaoMatricula;
    private String idTipoIntegralizacao;
    private Integer idTurma;
    //Media final da disciplina (Pode assumir o valor de String, caso seja do tipo conceito, ou Float, caso contrário)
    private Object mediaFinal;
    private Integer periodo;
    private Double recuperacao;

    public MatriculaComponenteUfrnDTO(){
        
    }
    
    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Boolean getConceito() {
        return conceito;
    }

    public void setConceito(Boolean conceito) {
        this.conceito = conceito;
    }

    public Integer getFaltas() {
        return faltas;
    }

    public void setFaltas(Integer faltas) {
        this.faltas = faltas;
    }

    public Integer getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
    }

    public Integer getIdDiscente() {
        return idDiscente;
    }

    public void setIdDiscente(Integer idDiscente) {
        this.idDiscente = idDiscente;
    }

    public Integer getIdMatriculaComponente() {
        return idMatriculaComponente;
    }

    public void setIdMatriculaComponente(Integer idMatriculaComponente) {
        this.idMatriculaComponente = idMatriculaComponente;
    }

    public Integer getIdSituacaoMatricula() {
        return idSituacaoMatricula;
    }

    public void setIdSituacaoMatricula(Integer idSituacaoMatricula) {
        this.idSituacaoMatricula = idSituacaoMatricula;
    }

    public String getIdTipoIntegralizacao() {
        return idTipoIntegralizacao;
    }

    public void setIdTipoIntegralizacao(String idTipoIntegralizacao) {
        this.idTipoIntegralizacao = idTipoIntegralizacao;
    }

    public Integer getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(Integer idTurma) {
        this.idTurma = idTurma;
    }

    public Object getMediaFinal() {
        return mediaFinal;
    }

    public void setMediaFinal(Object mediaFinal) {
        this.mediaFinal = mediaFinal;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public Double getRecuperacao() {
        return recuperacao;
    }

    public void setRecuperacao(Double recuperacao) {
        this.recuperacao = recuperacao;
    }
    
    
    
}
