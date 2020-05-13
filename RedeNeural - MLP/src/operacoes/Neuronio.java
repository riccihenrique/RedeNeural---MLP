/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operacoes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Alexandre
 */
public class Neuronio {
       
    private double net, erro, i, gradiente;

    public Neuronio() {
        this.net = 0;
        this.erro = 0;
        this.net = 0;
        this.gradiente = 0;
    }

    public double getNet() {
        return net;
    }

    public void setNet(double net) {
        this.net = net;
    }

    public double getErro() {
        return erro;
    }

    public void setErro(double erro) {
        this.erro = erro;
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
    }

    public double getGradiente() {
        return gradiente;
    }

    public void setGradiente(double gradiente) {
        this.gradiente = gradiente;
    }
       
       
    

    
          
}
