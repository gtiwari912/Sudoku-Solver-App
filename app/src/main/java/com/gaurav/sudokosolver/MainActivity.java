package com.gaurav.sudokosolver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText inputNumber;
    TableLayout gameBoard;
    Button btnSolve;
    Button btnReset;
    int grid_size= 9;
    int[][] myBoard=new int[grid_size][grid_size];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameBoard= findViewById(R.id.MainBoard);
        btnSolve= findViewById(R.id.btnSolve);
        inputNumber= findViewById(R.id.inputNumber);
        btnReset= findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGrid();
                setUpBoard();
            }
        });
        resetGrid();
        setUpBoard();

        btnSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (solveBoard(myBoard,0,0)){
                    for (int i= 0;i<gameBoard.getChildCount();i++){
                        TableRow row = (TableRow)gameBoard.getChildAt(i);
                        for (int j=0;j<row.getChildCount();j++){
                            TextView cell = (TextView) row.getChildAt(j);
                            cell.setText(myBoard[i][j]+"");
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"The Board is UnsolveAble",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean solveBoard(int[][]grid, int row, int col) {
        if (row == grid_size - 1 && col == grid_size)
            return true;
        if (col == grid_size) {
            row++;
            col = 0;
        }

        if (grid[row][col] != 0)
            return solveBoard(grid, row, col + 1);

        for (int num = 1; num < 10; num++) {
            if (isSafe(grid, row, col, num)) {

                grid[row][col] = num;

                if (solveBoard(grid, row, col + 1))
                    return true;
            }
            /* removing the assigned num , since our
               assumption was wrong , and we go for next
               assumption with diff num value   */
            grid[row][col] = 0;
        }
        return false;
    }

    private boolean isSafe(int[][] grid, int row, int col, int num) {
        for (int x=0;x<=8;x++){
            if (grid[row][x]==num){
                return false;
            }
        }
        for (int x=0;x<=8;x++){
            if (grid[x][col]==num){
                return false;
            }
        }
        int startRow = row-row%3;
        int startCol = col - col%3;

        for (int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if (grid[i+startRow][j+startCol]==num){
                    return false;
                }
            }
        }
        return true;
    }

    private View.OnClickListener Move(int i, int j, TextView cell) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(inputNumber.getText())){
                    Boolean checkRange = Integer.parseInt(inputNumber.getText().toString())>-1 && Integer.parseInt(inputNumber.getText().toString())<10;
                    if (checkRange){
                        Log.i("TAG912","IF me aa rha hu");
                        myBoard[i][j]= Integer.parseInt(inputNumber.getText().toString());
                        cell.setText(inputNumber.getText().toString());
                    }else {
                        Toast.makeText(getApplicationContext(),"Number should be between 0 to 10",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Enter the valid Number.",Toast.LENGTH_SHORT).show();
                }
                inputNumber.setText("");
            }
        };
    }


    private void resetGrid() {
        for (int i= 0;i<grid_size;i++){
            for (int j=0;j<grid_size;j++){
                myBoard[i][j]=0;
            }
        }
    }
    private void setUpBoard(){
        for (int i= 0;i<gameBoard.getChildCount();i++){
            TableRow row = (TableRow)gameBoard.getChildAt(i);
            for (int j=0;j<row.getChildCount();j++){
                TextView cell = (TextView) row.getChildAt(j);
                cell.setText("0");
                cell.setOnClickListener(Move(i,j,cell));
            }
        }
    }
}