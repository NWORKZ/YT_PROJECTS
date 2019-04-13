package com.nxiv.inlaypresentor.presentation;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Slide {

    public List<Widget> widgets;

    public Slide(){
        widgets = new ArrayList<>();
    }
    public void addWidget(Widget widget){
        widgets.add(widget);
    }

    public void removeWidget(Widget widget){
        widgets.remove(widget);
    }

    public void removeWidget(int index){
        widgets.remove(index);
    }

    public Widget getWidget(int index){
        return widgets.get(index);
    }
}
