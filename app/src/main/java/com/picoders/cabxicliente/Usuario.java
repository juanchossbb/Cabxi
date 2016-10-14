package com.picoders.cabxicliente;

import android.os.Bundle;

/**
 * Created by juancho on 13/10/16.
 */

public class Usuario {
    String ID,EMAIL,NOMBRE,APELLIDO,IMAGEN,TELEFONO;

    public Usuario(){

        this.ID=null;
        this.EMAIL=null;
        this.NOMBRE=null;
        this.APELLIDO=null;
        this.IMAGEN=null;
        this.TELEFONO=null;
    }
    public Usuario(Bundle bunble){
        this.ID=bunble.getString("id");
        this.EMAIL=bunble.getString("correo");
        this.NOMBRE=bunble.getString("nombres");
        this.APELLIDO=bunble.getString("apellidos");
        this.IMAGEN=bunble.getString("imagen");
        this.TELEFONO=bunble.getString("telefono");
    }

    public Bundle getInfo(){
        Bundle bundle= new Bundle();
        bundle.putString("id",ID);
        bundle.putString("correo",EMAIL);
        bundle.putString("nombres",NOMBRE);
        bundle.putString("apellidos",APELLIDO);
        bundle.putString("imagen",IMAGEN);
        bundle.putString("telefono",TELEFONO);

        return  bundle;
    }
}
