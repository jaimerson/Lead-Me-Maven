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
public class DiscenteUfrnDTO {
    private Integer anoIngresso;
    private String cpfCnpj;
    private Integer idCurso;
    private Integer idDiscente;
    private Integer matricula;
    private String nomeCurso;
    private String nomeDiscente;
    private Integer periodoIngresso;
    private String siglaNivel;

    public Integer getAnoIngresso() {
        return anoIngresso;
    }

    public void setAnoIngresso(Integer anoIngresso) {
        this.anoIngresso = anoIngresso;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer getIdDiscente() {
        return idDiscente;
    }

    public void setIdDiscente(Integer idDiscente) {
        this.idDiscente = idDiscente;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getNomeDiscente() {
        return nomeDiscente;
    }

    public void setNomeDiscente(String nomeDiscente) {
        this.nomeDiscente = nomeDiscente;
    }

    public Integer getPeriodoIngresso() {
        return periodoIngresso;
    }

    public void setPeriodoIngresso(Integer periodoIngresso) {
        this.periodoIngresso = periodoIngresso;
    }

    public String getSiglaNivel() {
        return siglaNivel;
    }

    public void setSiglaNivel(String siglaNivel) {
        this.siglaNivel = siglaNivel;
    }
    
    
}
