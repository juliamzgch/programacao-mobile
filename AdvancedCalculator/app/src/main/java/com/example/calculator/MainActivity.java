package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0;
    Button btnSum, btnMinus, btnMulti, btnDivi, btnEqual, btnClear, btnDot;
    TextView result;
    String operator = "";
    boolean isNewOp = true;

    private String currentNumber = "";
    private String leftOperand = "";
    private String rightOperand = "";
    private String currentOperator = "";
    private double calculationResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn0 = findViewById(R.id.btn0);
        result = findViewById(R.id.result);

        btnSum = findViewById(R.id.btnSum);
        btnMinus = findViewById(R.id.btnMinus);
        btnMulti = findViewById(R.id.btnMulti);
        btnDivi = findViewById(R.id.btnDivi);
        btnEqual = findViewById(R.id.btnEqual);
        btnClear = findViewById(R.id.btnClear);
        btnDot = findViewById(R.id.btnDot);

        btn0.setOnClickListener(v -> onNumberClick("0"));
        btn1.setOnClickListener(v -> onNumberClick("1"));
        btn2.setOnClickListener(v -> onNumberClick("2"));
        btn3.setOnClickListener(v -> onNumberClick("3"));
        btn4.setOnClickListener(v -> onNumberClick("4"));
        btn5.setOnClickListener(v -> onNumberClick("5"));
        btn6.setOnClickListener(v -> onNumberClick("6"));
        btn7.setOnClickListener(v -> onNumberClick("7"));
        btn8.setOnClickListener(v -> onNumberClick("8"));
        btn9.setOnClickListener(v -> onNumberClick("9"));
        btn0.setOnClickListener(v -> onNumberClick("0"));
        btnDot.setOnClickListener(v -> onNumberClick("."));

        btnSum.setOnClickListener(v -> operatorClick("+"));
        btnMinus.setOnClickListener(v -> operatorClick("-"));
        btnMulti.setOnClickListener(v -> operatorClick("*"));
        btnDivi.setOnClickListener(v -> operatorClick("/"));
        btnEqual.setOnClickListener(v -> onEqualClick());
        btnClear.setOnClickListener(v -> onClearClick());

    }
    
    private void onEqualClick() {
        if (leftOperand.isEmpty() || currentNumber.isEmpty() || currentOperator.isEmpty()) {
            return;
        }
        
        rightOperand = currentNumber;
        double leftNum = Double.parseDouble(leftOperand);
        double rightNum = Double.parseDouble(rightOperand);
        
        switch (currentOperator) {
            case "+":
                calculationResult = leftNum + rightNum;
                break;
            case "-":
                calculationResult = leftNum - rightNum;
                break;
            case "*":
                calculationResult = leftNum * rightNum;
                break;
            case "/":
                if (rightNum == 0) {
                    Toast.makeText(this, "não há divisão por zero", Toast.LENGTH_SHORT).show();
                    onClearClick();
                    return;
                }
                calculationResult = leftNum / rightNum;
                break;
        }
        
        result.setText(String.valueOf(calculationResult));
        
        leftOperand = String.valueOf(calculationResult);
        currentNumber = "";
        currentOperator = "";
        isNewOp = true;
    }
    
    private void onClearClick() {
        currentOperator = "";
        currentNumber = "";
        calculationResult = 0;
        leftOperand = "";
        rightOperand = "";
        result.setText("0");
    }
    private void onDotClick(String num) {
        if (!currentNumber.contains(".")) {
            if (currentNumber.isEmpty()) {
                currentNumber = "0.";
            } else {
                currentNumber += ".";
            }
            result.setText(currentNumber);
        }
    }
    
    private void onNumberClick(String num) {
        currentNumber += num;
        result.setText(currentNumber);
    }

    private void operatorClick(String op) {
        if (!currentOperator.isEmpty() && !currentNumber.isEmpty()) {
            onEqualClick();
        }
        if (!currentNumber.isEmpty()) {
            leftOperand = currentNumber;
            currentNumber = "";
            currentOperator = op;
        } else if (!leftOperand.isEmpty()) {
            currentOperator = op;
        }
    }

    

}