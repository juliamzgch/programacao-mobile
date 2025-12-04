package com.example.profile;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.File;
import java.util.Calendar;
import java.util.prefs.Preferences;


public class MainActivity extends AppCompatActivity {

    /* Constantes para facilitar o acesso aos nomes dos valores a guardar */
    private static final String VALOR_NOME = "NAME";
    private static final String VALOR_EMAIL = "EMAIL";
    private static final String VALOR_CAMINHO_FOTO = "PHOTO_PATH";
    private static final String VALOR_SENHA = "PASSWORD";
    private static final String VALOR_ANO = "YEAR";
    private static final String VALOR_MES = "MONTH";
    private static final String VALOR_DIA = "DAY";

    private static final String VALOR_CAMPEAO = "CHAMPION";
    private static final String VALOR_GENERO = "GENDER";
    private static final String VALOR_TIPO_USUARIO = "USER_TYPE";

    /* Constante que indentifica o pedido de permissão para ler ficheiros */
    private static final int PERMISSAO_LER_FICHEIROS = 1000;
    /* Request code para a escolha de uma imagem da galeria */
    private static final int IMAGE_PICKER_SELECT = 1001;
    //atributo para manter o nome da imagem entre chamadas de metodos
    private String photoPath;
    private Calendar calendar;
    private int currentYear, currentMonth, currentDay;
    private int ano, mes, dia;
    private EditText dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        Spinner spinner = findViewById(R.id.spinnerUserType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_type_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //associar um DatePickerDialog ao clique na DateView
        dateView = findViewById(R.id.editBirthDate);
        dateView.setInputType(InputType.TYPE_NULL);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view,
                                                  int year,
                                                  int month,
                                                  int dayOfMonth) {
                                ano = year;
                                mes = month;
                                dia = dayOfMonth;
                                showDate(ano, mes + 1, dia);
                            }
                        }, currentYear, currentMonth, currentDay);
                picker.show();
            }
        });


        //quando a aplicação é lançada, vamos tentar ler possiveis valores guardados
        lerValores();
        //vamos associar o método guardarValores() ao clique no botao
        Button buttonGuardar = findViewById(R.id.buttonSave);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarValores();
            }
        });
        //associar o método carregarImagem() no clique na imageView (prop. isClickable = true)
        ImageView imgView = findViewById(R.id.imagePhoto);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedirImagemComPermissoes();
            }
        });
    }

    public void lerValores() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //o método getString() recebe duas strings: o nome do valor e o valor
        //por omissão (se não houver valor guardado)
        String nomeGuardado = sharedPref.getString(VALOR_NOME, "");
        String emailGuardado = sharedPref.getString(VALOR_EMAIL, "");
        photoPath = sharedPref.getString(VALOR_CAMINHO_FOTO, "");
        String senhaGuardada = sharedPref.getString(VALOR_SENHA, "");
        int anoGuardado = sharedPref.getInt(VALOR_ANO, currentYear);
        int mesGuardado = sharedPref.getInt(VALOR_MES, currentMonth);
        int diaGuardado = sharedPref.getInt(VALOR_DIA, currentDay);
        boolean campeaoGuardado = sharedPref.getBoolean(VALOR_CAMPEAO, true);
        int generoGuardado = sharedPref.getInt(VALOR_GENERO, 1);
        int tipoGuardado = sharedPref.getInt(VALOR_TIPO_USUARIO, 0);

        //obter referencias para widgets
        EditText editNome = findViewById(R.id.editName);
        EditText editEmail = findViewById(R.id.editEmail);
        EditText editSenha = findViewById(R.id.editPassword);
        Switch switchCampeao = findViewById(R.id.switchChampion);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Spinner spinner = findViewById(R.id.spinnerUserType);

        //alterar o texto nas widgets
        editNome.setText(nomeGuardado);
        editEmail.setText(emailGuardado);
        carregarImagem(photoPath);
        editSenha.setText(senhaGuardada);
        showDate(anoGuardado, mesGuardado + 1, diaGuardado);
        switchCampeao.setChecked(campeaoGuardado);
        radioGroup.check(generoGuardado);
        spinner.setSelection(tipoGuardado);
    }


    public void guardarValores() {
        //obter referencia para widgets
        EditText editNome = findViewById(R.id.editName);
        EditText editEmail = findViewById(R.id.editEmail);
        EditText editSenha = findViewById(R.id.editPassword);
        Switch switchCampeao = findViewById(R.id.switchChampion);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Spinner spinner = findViewById(R.id.spinnerUserType);

        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
        int genero = radioGroup.getCheckedRadioButtonId();
        boolean campeao = switchCampeao.isChecked();
        int userType = spinner.getSelectedItemPosition();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() ||
                !(radioGroup.isSelected()) || (userType == 0)) {
            Toast.makeText(this, R.string.toast_empty_fields, Toast.LENGTH_LONG).show();
            return;
        }


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //guardar valores
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString(VALOR_NOME, nome);
        edit.putString(VALOR_EMAIL, email);
        edit.putString(VALOR_CAMINHO_FOTO, photoPath);
        edit.putString(VALOR_SENHA, senha);
        edit.putInt(VALOR_ANO, ano);
        edit.putInt(VALOR_MES, mes);
        edit.putInt(VALOR_DIA, dia);
        edit.putBoolean(VALOR_CAMPEAO, campeao);
        edit.putInt(VALOR_GENERO, genero);
        edit.putInt(VALOR_TIPO_USUARIO, userType);
        //esta instrução é que vai guardar os valores
        edit.commit();
        //notificar utilizador da concretização da operação
        Toast.makeText(this, getResources().getText(R.string.info_saved), Toast.LENGTH_SHORT).show();
    }

    public void pedirImagem() {
        //ira lançar uma activity do sistema e posteriormente
        // chamará o método onActivityResult (abaixo)
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, IMAGE_PICKER_SELECT);
    }

    private void pedirImagemComPermissoes() {
        //Verificar se a app tem acesso de leitura aos ficheiros do utilizador
        //Em caso negativo temos de tratar de pedir as permissões necessárias.
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Se o utilizador rejeitou a permissão anteriormente talvez seja bom explicar porque
            //é que a permissão é necessária.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //Mostrar um diálogo que explica a razão para pedir a permissão e voltar
                //a pedir a permissão na esperança que o utilizador aceite desta vez.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_read_file_permission)
                        .setCancelable(false)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                pedirPermissoes();
                            }
                        });
                builder.create().show();
            } else {
                //Na primeira vez simplesmente pedimos o acesso ao ficheiros do utilizador na
                //esperança que ele aceite.
                pedirPermissoes();
            }
            //Em caso afirmativo simplesmente pedir a imagem
        } else {
            pedirImagem();
        }
    }

    private void carregarImagem(String imgPath) {
        ImageView imgView = findViewById(R.id.imagePhoto);
        if (imgPath.isEmpty()) { //usar imagem 'dummy'
            imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.profile_placeholder, null));
        } else {
            File imgFile = new File(imgPath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgView.setImageBitmap(myBitmap);
            }
        }
    }

    public void pedirPermissoes() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_LER_FICHEIROS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSAO_LER_FICHEIROS: {
                //se o pedido foi cancelado
                // o array de resultados está vazio
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permissão aceite.
                    Toast.makeText(this, R.string.toast_read_file_permission_granted, Toast.LENGTH_SHORT).show();
                    //lançar o pedido de escolha da imagem.
                    pedirImagem();
                } else {
                    //permissão rejeitada, não poderá
                    //alterar a sua imagem de perfil
                    Toast.makeText(this, R.string.toast_read_file_permission_denied, Toast.LENGTH_SHORT). show();
                }
            }
        }
    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String [] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = MainActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            photoPath = cursor.getString(columnIndex);
            cursor.close();
            carregarImagem(photoPath);
        }
    }
}