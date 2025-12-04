package com.example.lab031;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private AudioManager mAudioManager;
    private boolean mModoSilencioso = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // inicialzar AudioManager
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        adicionaEventoBotao();
        verificaEstadoRinger();
        alteraIconSonoro();

    }

    public void onResume() {
        super.onResume();
        // Obter o NotificationManager para verificar se temos
        // permissão para alterar o ringer mode
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // Só necessitamos de pedir autorização no Android 6.0 Marshmallow
        // ou superior e só se realmente não tivermos permissões.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            // Construímos o intent que nos permite
            // aceder às permissões que temos de dar à app.
            Intent intent =
                    new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            // Lançamos a actividade com base no intent.
            startActivity(intent);
        }
        verificaEstadoRinger();
        alteraIconSonoro();
    }

    // Adiciona o listener ao botaoOnOff. Sempre que o botão for premido,
    // o código fornecido será executado. Isto é programação orientada a eventos.
    private void adicionaEventoBotao() {
        Button btnToggle = (Button) findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModoSilencioso) {
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    mModoSilencioso = false;
                } else {
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    mModoSilencioso = true;
                }
                alteraIconSonoro();
            }
        });
    }

    // Este método afeta ao atributo <b>mModoSilencio</b>
    // o valor <i>true</i> se o ringer estiver
    // em modo silencio ou em vibração e <i>false</i> se estiver em modo normal.
    private void verificaEstadoRinger() {

        //obter estado do ringer
        int estado = mAudioManager.getRingerMode();
        //comparar com as constantes de classes pré definidas

        // RINGER_MODE_NORMAL = 2
        // RINGER_MODE_SILENT
        // modo silencioso se o estado for diferente do normal
        // porque ambos sao boolean
        mModoSilencioso = estado != AudioManager.RINGER_MODE_NORMAL;
    }

    // Este método é responsável por colocar o icon correto mediante o estado do ringer
    private void alteraIconSonoro() {
        ImageView imgMode = (ImageView) findViewById(R.id.imgMode);
        TextView txtMode = (TextView) findViewById(R.id.txtMode);
        if (!mModoSilencioso) {
            imgMode.setImageResource(R.drawable.unmuted);
            txtMode.setText(R.string.normal_mode);
        } else {
            imgMode.setImageResource(R.drawable.muted);
            txtMode.setText(R.string.silent_mode);
        }
    }

}