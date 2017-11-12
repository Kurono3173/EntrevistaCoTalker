package com.example.crono.programacotalker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<String> listAUX = new ArrayList<String>();
    NumberPicker np;
    EditText editText;
    Button bt, bt2;
    TextView tv;
    ListView lista;
    /*Obtencion de elementos en el layout */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tvE);
        np =  findViewById(R.id.np);
        editText =  findViewById(R.id.et);
        bt =  findViewById(R.id.bt);
        bt2 =  findViewById(R.id.bt2);
        lista = (ListView) findViewById(R.id.mobile_list);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultaAPI();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Algoritmo();
            }
        });

        /*Se coloco de 0 a 100 como dice las instrucciones, pero la api solo soporta de 1 a 40 */
        np.setMinValue(0);
        np.setMaxValue(100);
        np.setWrapSelectorWheel(true);
    }
    /*Se consulta a la API solo si el numero es entre 1 y 40 */
    public void consultaAPI(){

        if(np.getValue()>40 || np.getValue()==0){
            editText.setText("Debe elegir entre 1 y 40");
        }
        else{
            APIRequest(np.getValue());
        }

    }
    /*El algoritmo del replaceAsterisk, primero contamos el numero de * para ver si se puede
     * realizar o no, en caso afirmativo, genero en una lista todas las posibilidades de
      * reemplazo de asterisco con print01PermutationsUpToLength y luego recorro la respuesta
      * de la api y por cada "*" lo reemplzado con el numero que corresponde segun la iteracion
      * luego lo paso a un arreglo y este se lo paso a un adaptador del listview para su mejor
      * visualizacion*/
    public void Algoritmo(){

        int aux=0;
        for(int x=0;x<editText.getText().length();x++){
            if(editText.getText().charAt(x)=='*'){
                aux++;
            }
        }
        if(aux>0){
            tv.setVisibility(View.VISIBLE);
            List<String> list = new ArrayList<String>();
            listAUX = new ArrayList<String>();
            print01PermutationsUpToLength("",aux,listAUX);
            int aux2 = aux;
            aux =0;
            int contador=0;
            while(aux!=aux2*2){
                String listaAux="";
                for(int x=0;x<editText.getText().length();x++){

                    if(editText.getText().charAt(x)=='*'){
                        listaAux = listaAux+listAUX.get(aux).charAt(contador);
                        contador++;

                    }
                    else{
                        listaAux = listaAux+editText.getText().toString().charAt(x);
                    }
                }
                contador=0;
                list.add(listaAux);
                aux++;
            }
            String[] list1 = new String[list.size()];
            for(int x=0;x<list.size();x++){

                list1[x] = list.get(x);

            }
            lista.setVisibility(View.VISIBLE);
            com.example.crono.programacotalker.data.listAdapter adapter;
            ArrayList<String> dataItems = new ArrayList<String>();
            List<String> dataTemp = Arrays.asList(list1);
            dataItems.addAll(dataTemp);
            adapter = new com.example.crono.programacotalker.data.listAdapter(MainActivity.this, dataItems);
            lista.setAdapter(adapter);
            tv.setVisibility(View.INVISIBLE);
        }else{
            lista.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.VISIBLE);
            tv.setText("Debe tener almenos un *");
        }
    }

    /*Se hace el request a la API usando Volley y manejando sus respectivos errores*/
    public void APIRequest(int length){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://s8wojkby0k.execute-api.sa-east-1.amazonaws.com/prod/practica";
        JSONObject requestObject=new JSONObject();
        try{
            requestObject.put("length",length);
        }
        catch (JSONException e){

        }
        final String mRequestBody=requestObject.toString();
        StringRequest arrayRequest=new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONArray array=new JSONArray(response);
                            //Aqui colocamos la respuesta obtenida, pasandola primero por un parse
                            editText.setText(parse(array));
                        }catch(JSONException e){

                        }
                    }},
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        editText.setText(error.getMessage());
                    }}
        ){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };
        queue.add(arrayRequest);
    }
    /*Parsiamos la respuesta de forma que queden unidos los  "1" "0" y "*" */
    public String parse(JSONArray array){
        String result="";
        for(int i=0;i<array.length();i++){
            try{
                result+=array.get(i);

            }catch (JSONException e){

            }
        }
        return result;
    }

    /*Funcion recusriva para obtener en una lista todas las posibilidades de 0 y 1 segun el numero de "*" */
    public static void print01PermutationsUpToLength(final String currentString, final int upTo, List<String> listAUX) {
        if (upTo == 0) {
            listAUX.add(currentString);
            return;
        }
        print01PermutationsUpToLength(currentString + "0", upTo - 1, listAUX);
        print01PermutationsUpToLength(currentString + "1", upTo - 1,listAUX);
    }
}
