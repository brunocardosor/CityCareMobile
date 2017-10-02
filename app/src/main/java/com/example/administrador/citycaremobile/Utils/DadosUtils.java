package com.example.administrador.citycaremobile.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrador.citycaremobile.Modelo.Categoria;
import com.example.administrador.citycaremobile.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrador on 24/09/2017.
 */

public class DadosUtils extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "citycare_db";
    private static final int VERSAO = 1;
    private String categorias[] = {"Categoria", "Buraco nas vias", "Entulho na calçada/via pública", "Limpeza urbana", "Esgoto a céu aberto", "Bloqueio na via", "Estacionamento irregular", "Ocupação irregular de área pública", "Lâmpada apagada à noite", "Mato alto", "Bueiro entupido", "Ponto de alagamento", "Condição sanitária irregular", "Calçada irregular", "Falta de rampa de acessibilidade", "Foco de dengue", "Poluição sonora", "Equipamento público danificado", "Poda/retirada de árvore", "Faixa de pedestre inexistente", "Imóvel abandonado", "Falta de energia", "Falta de água", "Iluminação pública irregular", "Fiação irregular", "Emissão de fumaça preta", "Transporte Público Irregular", "Placa de sinalização quebrada", "Vazamento de água", "Queimada irregular", "Infestação de roedores", "Ponto de assalto/roubo", "Recuperação de estradas", "Lâmpada acesa de dia", "Estação de ônibus/trem/metrô danificada", "Falta de assentos na sala de embarque", "Estabelecimento sem saída de emergência", "Aterro sanitário irregular", "Ar-condicionado com defeito", "Ciclovia/ciclofaixa mal sinalizada", "Ponto de tráfico de drogas", "Demora na fila do check-in", "Pesca ilegal", "Serviço de Assistência Técnica", "Cercamento de reserva legal", "Ponto de ônibus danificado", "Patrimônio histórico em risco", "Tomada com defeito no aeroporto", "Falta de táxi", "Bicicletário danificado", "Demora na entrega de bagagens em aeroporto", "Lixeira quebrada", "Estabelecimento sem nota fiscal", "Calçada inexistente", "Estádio danificado", "Aeroporto com instalações danificadas", "Semáforo quebrado", "Metrô/trem danificado", "Praia suja", "Caça ilegal", "Falta de carrinhos de bagagem", "Estabelecimento sem alvará", "Ônibus danificado", "Desmatamento irregular", "Aeroporto superlotado", "Demora na fila do Raio X", "Maus tratos a animais", "Estabelecimento com acessibilidade irregular", "Sanitário sujo no aeroporto", "Painel de informação de vôos desligado/danificado", "Acesso problemático ao estádio", "Ônibus/trem/metrô superlotado", "Desmatamento Ilegal", "Caixa eletrônico com defeito", "Veículo abandonado", "Recuperação de Ponte", "Poluição", "Bueiro sem tampa", "Atraso excessivo em voo", "Estádio com acessibilidade irregular", "Bagagem danificada", "Presença de cambista em estádio", "Sujeira no aeroporto", "Ponto de prostituição de menores", "Demora na entrega de bagagem", "Utilização irregular de agrotóxico", "Caça predatória", "Passarela irregular"};
    private Context context;

    public DadosUtils(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS categoria(" +
                "id_categoria INTEGER primary key autoincrement," +
                "descricao_categoria VARCHAR(45) not null);");

        db.execSQL("CREATE TABLE IF NOT EXISTS estado(" +
                "id_estado INTEGER primary key autoincrement," +
                "estado varchar(20) not null" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS cidade(" +
                "id_cidade INTEGER primary key autoincrement," +
                "cidade varchar(50) not null," +
                "cidade_id_estado INTEGER not null," +
                "foreign key(cidade_id_estado) references cidade(id_estado)" +
                ");");
        try {
            inserirEstados(db);
            inserirMunicipios(db);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < categorias.length; i++) {
            db.execSQL("INSERT INTO categoria (descricao_categoria) values('" + categorias[i] + "')");
        }
        criarDiretorio("user");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public List<String> listarCategoria() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("categoria", null, null, null, null, null, null);
        List<String> categorias = new ArrayList<String>();
        categorias.add("Categorias");
        if (c.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(c.getInt(c.getColumnIndex("id_categoria")));
                categoria.setDescricaoCategoria(c.getString((c.getColumnIndex("descricao_categoria"))));
                categorias.add(categoria.toString());
            } while (c.moveToNext());
        }
        return categorias;
    }

    public List<String> listarCidades(int posicao) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("cidade", null, "cidade_id_estado='" + posicao + "'", null, null, null, null);
        List<String> cidades = new ArrayList<String>();
        cidades.add("Cidade");
        if (c.moveToFirst()) {
            do {
                String cidade = c.getString(c.getColumnIndex("cidade"));
                cidades.add(cidade);
            } while (c.moveToNext());
        }
        return cidades;
    }

    public List<String> listarEstados() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("estado", null, null, null, null, null, null);
        List<String> estados = new ArrayList<String>();
        estados.add("Estado");
        if (c.moveToFirst()) {
            do {
                String estado;
                estado = c.getString(c.getColumnIndex("estado"));
                estados.add(estado);

            } while (c.moveToNext());
        }
        return estados;
    }


    public void inserirMunicipios(SQLiteDatabase db) {
        final Resources res = context.getResources();
        InputStream is = res.openRawResource(R.raw.estado);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] strings = TextUtils.split(linha, ";");
                db.execSQL(linha);
                Log.i("TableCidade", "Erro : " + strings[0].trim());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void inserirEstados(SQLiteDatabase db) throws IOException {
        final Resources res = context.getResources();
        InputStream is = res.openRawResource(R.raw.cidade);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] strings = TextUtils.split(linha, ";");
                db.execSQL(linha);
                Log.e("Teste", "Erro : " + strings[0].trim());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void criarDiretorio(String nomeDir) {
        File newdir = context.getDir(nomeDir, Context.MODE_PRIVATE);
        if (!newdir.exists()) {
            if (!newdir.mkdir()) {
                Log.i("Diretory", "Não foi possível criar o diretório");
            }
        }
    }
}
