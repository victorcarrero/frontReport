/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontReport.valueobject;

/**
 *
 * @author victor
 */
public class EstadoColumnaVO {
    
    private int idEstadoColumna;
    private String estadoColumna;
    
    public EstadoColumnaVO() {
        
    }

    public EstadoColumnaVO(int idEstadoColumna, String estadoColumna) {
        this.idEstadoColumna = idEstadoColumna;
        this.estadoColumna = estadoColumna;
    }

    public int getIdEstadoColumna() {
        return idEstadoColumna;
    }

    public void setIdEstadoColumna(int idEstadoColumna) {
        this.idEstadoColumna = idEstadoColumna;
    }

    public String getEstadoColumna() {
        return estadoColumna;
    }

    public void setEstadoColumna(String estadoColumna) {
        this.estadoColumna = estadoColumna;
    }
    
    
    
}
