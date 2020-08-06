package com.example.acerteonumero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_Enviar ;
    Button btn_NovaPartida;
    TextView txt_Numero;
    EditText edt_Palpite;

    View display1;
    View display2;
    View display3;

    String _numero;
    String _numeroDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Enviar = findViewById(R.id.btn_Enviar);
        btn_NovaPartida = findViewById(R.id.btn_NovaPartida);
        txt_Numero = findViewById(R.id.txt_Numero);
        edt_Palpite = findViewById(R.id.edt_Palpite);
        display1 = findViewById(R.id.display1);
        display2 = findViewById(R.id.display2);
        display3 = findViewById(R.id.display3);

        this.novaPartida();

        btn_Enviar.setOnClickListener(this);
        btn_NovaPartida.setOnClickListener(this);
    }

    //Método para iiniciar uma nova partida, resetando os valores e buscando um novo valor no endpoint
    private void novaPartida(){
        resetarPartida();

        String resposta = null;
        try {
            resposta = new HttpService().execute().get();
            JSONObject obj = new JSONObject(resposta);
            String valor = "";

            //Caso a resposta contenha o StatusCode, significa que houve um problema com a requisicao, sendo recuperado o código do status e desabilitando o jogo, forçando o jogador a iniciar uma nova partida
            if(resposta.contains("StatusCode")){
                valor = obj.getString("StatusCode");
                txt_Numero.setText("Erro");
                btn_Enviar.setEnabled(false);
                edt_Palpite.setEnabled(false);
            }else if(resposta.contains("value")){
                valor = obj.getString("value");
            }
            _numero = valor;

        } catch (JSONException e) {
            txt_Numero.setText(e.getMessage());
        } catch (ExecutionException e) {
            txt_Numero.setText(e.getMessage());
        } catch (InterruptedException e) {
            txt_Numero.setText(e.getMessage());
        }
        AtualizarDisplay();
    }

    //Método para resetar os valores e estados da variáveis e campos utilizados
    private void resetarPartida(){
        _numero = "0";
        _numeroDisplay = "0";
        txt_Numero.setText("");
        edt_Palpite.setText("");
        btn_Enviar.setEnabled(true);
        edt_Palpite.setEnabled(true);
        btn_NovaPartida.setVisibility(View.GONE);
        resetaDisplay();
    }

    //Método para comparar os valores digitado pelo usuário e o valor obtido no acesso ao endpoint
    private void compararValores(){
        _numeroDisplay = edt_Palpite.getText().toString();
        AtualizarDisplay();
        int palpite = Integer.parseInt(_numeroDisplay);
        int numero = Integer.parseInt(_numero);

        if(palpite > numero)
            txt_Numero.setText("É menor");
        else if(palpite < numero)
            txt_Numero.setText("É maior");
        else {
            txt_Numero.setText("Acertou!");
            btn_NovaPartida.setVisibility(View.VISIBLE);
            btn_Enviar.setEnabled(false);
            edt_Palpite.setEnabled(false);
        }
    }

    //Método para para atualizar os valores em cada display e desabilita-los caso seja necessário
    private void AtualizarDisplay(){
        resetaDisplay();
        int numero = (_numeroDisplay == null) ? 0 : Integer.parseInt(_numeroDisplay);

        int unidade = numero % 10;
        int dezenas = (numero % 100) / 10;
        int centenas = numero / 100;

        if (_numeroDisplay == null || _numeroDisplay.length() == 1) {
            display2.setVisibility(View.GONE);
            display1.setVisibility(View.GONE);
        }else if (_numeroDisplay.length() == 2)
            display1.setVisibility(View.GONE);

        acendeNumerosDisplay(display1, centenas);
        acendeNumerosDisplay(display2, dezenas);
        acendeNumerosDisplay(display3, unidade);
    }

    //Método para resetar os 3 displays e habilitá-los novamente
    private void resetaDisplay(){

        display1.setVisibility(View.VISIBLE);
        display2.setVisibility(View.VISIBLE);
        display3.setVisibility(View.VISIBLE);

        resetaDisplay(display1);
        resetaDisplay(display2);
        resetaDisplay(display3);
    }

    //Método para resetar os 3 displays, ou seja, preenchêlos completamente ( Como se fosse um número 8 )
    private void resetaDisplay(View view){
        int colorOn = getResources().getColor(R.color.colorAccent);
        view.findViewById(R.id.a).setBackgroundColor(colorOn);
        view.findViewById(R.id.b).setBackgroundColor(colorOn);
        view.findViewById(R.id.c).setBackgroundColor(colorOn);
        view.findViewById(R.id.d).setBackgroundColor(colorOn);
        view.findViewById(R.id.e).setBackgroundColor(colorOn);
        view.findViewById(R.id.f).setBackgroundColor(colorOn);
        view.findViewById(R.id.g).setBackgroundColor(colorOn);
    }

    //Método para acender um display qualquer com um númeero desejado, informado como parâmetro
    //Os displays estarão completamente acesos, pontanto, este método irá apenas apagar os segmentos necessários para formar o número desejado
    private void acendeNumerosDisplay(View view, int numero){
        int corOff = getResources().getColor(R.color.colorOff);

        //Não temos o caso "8" pois  significa o display completamente aceso, não precisando apagar enum segmento
        switch (numero){
            case 0:
                view.findViewById(R.id.g).setBackgroundColor(corOff);
                break;
            case 1:
                view.findViewById(R.id.a).setBackgroundColor(corOff);
                view.findViewById(R.id.b).setBackgroundColor(corOff);
                view.findViewById(R.id.c).setBackgroundColor(corOff);
                view.findViewById(R.id.f).setBackgroundColor(corOff);
                view.findViewById(R.id.g).setBackgroundColor(corOff);
                break;
            case 2:
                view.findViewById(R.id.a).setBackgroundColor(corOff);
                view.findViewById(R.id.d).setBackgroundColor(corOff);
                break;
            case 3:
                view.findViewById(R.id.a).setBackgroundColor(corOff);
                view.findViewById(R.id.b).setBackgroundColor(corOff);
                break;
            case 4:
                view.findViewById(R.id.b).setBackgroundColor(corOff);
                view.findViewById(R.id.c).setBackgroundColor(corOff);
                view.findViewById(R.id.f).setBackgroundColor(corOff);
                break;
            case 5:
                view.findViewById(R.id.b).setBackgroundColor(corOff);
                view.findViewById(R.id.e).setBackgroundColor(corOff);
                break;
            case 6:
                view.findViewById(R.id.e).setBackgroundColor(corOff);
                break;
            case 7:
                view.findViewById(R.id.a).setBackgroundColor(corOff);
                view.findViewById(R.id.b).setBackgroundColor(corOff);
                view.findViewById(R.id.c).setBackgroundColor(corOff);
                view.findViewById(R.id.g).setBackgroundColor(corOff);
                break;
            case 9:
                view.findViewById(R.id.b).setBackgroundColor(corOff);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Enviar:
                compararValores();
                break;
            case R.id.btn_NovaPartida:
                novaPartida();
                break;
        }
    }
}
