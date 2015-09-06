package com.example.krystian892.truss;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.io.Serializable;

public class ArticleFragment extends Fragment {

    MainEditorView view = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(view == null) {
           // view = new MainEditorView(getActivity().getApplicationContext());
            Bundle bundle = ActivitySingleton.getActivity().getIntent().getExtras();
            if(bundle!=null) {
                if(bundle.containsKey("LOADEDGAME")){
                    String str = bundle.getString("LEVELID");
                   // Log.d("LEVEL NAME",str);
                    view = new GameView(getActivity().getApplicationContext(),str);
                    String s= bundle.getString("CONSTRUCTION");
                    Log.wtf("filepath",s+ " " + str);
                    view.setConstr(ConstructionLoader.readFromFile(s,str));
                }
                else if(bundle.containsKey("RAWRES")){
                    int id = bundle.getInt("RAWRES");
                    String str = bundle.getString("LEVELID");
                    //Log.d("LEVEL NAME",str);
                    view = new GameView(getActivity().getApplicationContext(),str);
                    view.setConstr(TextResourceLoader.load(this.getActivity().getApplicationContext(),id));
                }
                else view = new MainEditorView(getActivity().getApplicationContext());
                String s= bundle.getString("CONSTRUCTION");
                if (s!= null && !bundle.containsKey("LEVELID"))
                    if(s.charAt(0)!='#')
                        view.setConstr(ConstructionLoader.readFromFile(s));
                    else view.setConstr(ConstructionLoader.readFromAssets(s.substring(1)));



                    // view.constr.calculationsStarted=false;
            }
            else  view = new MainEditorView(getActivity().getApplicationContext());;

        }
        if(savedInstanceState != null){
            view.setConstr((Construction)savedInstanceState.getSerializable("CONSTRUCTION"));
            view.onTouch.commandQueue=(CommandQueue)savedInstanceState.getSerializable("COMMAND");
        }
        return view;
    }

    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("CONSTRUCTION", (Serializable) view.constr);
        outState.putSerializable("COMMAND", (Serializable)view.onTouch.commandQueue);
    }
    public void onRestoreInstanceState(final Bundle savedInstanceState){
        if(savedInstanceState != null){
            view.setConstr((Construction)savedInstanceState.getSerializable("CONSTRUCTION"));
            view.onTouch.commandQueue=(CommandQueue)savedInstanceState.getSerializable("COMMAND");
        }
    }




}
