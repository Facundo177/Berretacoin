package aed;

public class Handle {
    private int posicion;
    private Object puntero;

    public Handle(int posicion) {
        this.posicion = posicion;
    }
    public Handle(Object puntero) {
        this.puntero = puntero;
    }

    public int obtenerPosicion(){
        return posicion;
    }
    public Object obtenerPuntero(){
        return puntero;
    }

    public void modificarPosicion(int i){
        this.posicion = i;
    }
    public void modificarPuntero(Object o){
        this.puntero = o;
    }
    
} 
