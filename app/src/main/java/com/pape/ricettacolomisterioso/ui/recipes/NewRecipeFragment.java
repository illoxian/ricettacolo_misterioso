package com.pape.ricettacolomisterioso.ui.recipes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.pape.ricettacolomisterioso.databinding.FragmentNewRecipeBinding;
import com.pape.ricettacolomisterioso.models.Recipe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewRecipeFragment extends Fragment {
    private static final String TAG = "NewRecipeFragment";
    private Calendar c;
    private DatePickerDialog datePickerDialog;
    private List<String> CATEGORIES;
    private FragmentNewRecipeBinding binding;

    private Recipe recipe;
    private Date insertDate;
    private MutableLiveData<Long> insertId;

/*
    Button buttonAdd;
    LinearLayout container;
    TextView reList, info;

*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewRecipeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setHasOptionsMenu(true);
        return view;
    }



    private void addRecipe(){
        if(recipe==null) recipe = new Recipe();

        recipe.setRecipe_name(binding.textInputRecipeName.getText().toString());
        recipe.setRecipe_category(binding.textInputRecipeCategory.getText().toString());
        List<String> ingredients = new ArrayList<String>();
        List<String> steps = new ArrayList<String>();
        int nIngredients=0;
        int nSteps=0;
        for (int i =0; i<nIngredients; i++) {
            String curr = binding.newRecipeIngredientText.getText().toString();
            ingredients.add(curr);

        }
/*        for (int i=0; i<nSteps; i++) {
            String curr = binding.newRecipeStepText.getText().toString();
            steps.add(curr);
        }*/
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);


    }
/*


        private static final String[] NUMBER = new String[] {
                "One", "Two", "Three", "Four", "Five",
                "Six", "Seven", "Eight", "Nine", "Ten"
        };
        ArrayAdapter<String> adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, NUMBER);

            textIn = (AutoCompleteTextView)findViewById(R.id.textin);
            textIn.setAdapter(adapter);

            buttonAdd = (Button)findViewById(R.id.add);
            container = (LinearLayout) findViewById(R.id.container);
            reList = (TextView)findViewById(R.id.relist);
            reList.setMovementMethod(new ScrollingMovementMethod());
            info = (TextView)findViewById(R.id.info);
            info.setMovementMethod(new ScrollingMovementMethod());

            buttonAdd.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.row, null);
                    AutoCompleteTextView textOut = (AutoCompleteTextView)addView.findViewById(R.id.textout);
                    textOut.setAdapter(adapter);
                    textOut.setText(textIn.getText().toString());
                    Button buttonRemove = (Button)addView.findViewById(R.id.remove);

                    final View.OnClickListener thisListener = new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            info.append("thisListener called:\t" + this + "\n");
                            info.append("Remove addView: " + addView + "\n\n");
                            ((LinearLayout)addView.getParent()).removeView(addView);

                            listAllAddView();
                        }
                    };

                    buttonRemove.setOnClickListener(thisListener);
                    container.addView(addView);

                    info.append(
                            "thisListener:\t" + thisListener + "\n"
                                    + "addView:\t" + addView + "\n\n"
                    );

                    listAllAddView();
                }
            });
        }

        private void listAllAddView(){
            reList.setText("");

            int childCount = container.getChildCount();
            for(int i=0; i<childCount; i++){
                View thisChild = container.getChildAt(i);
                reList.append(thisChild + "\n");

                AutoCompleteTextView childTextView = (AutoCompleteTextView) thisChild.findViewById(R.id.textout);
                String childTextViewValue = childTextView.getText().toString();
                reList.append("= " + childTextViewValue + "\n");
            }
        }

    }
*/

}
