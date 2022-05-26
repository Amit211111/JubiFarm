/*
 * Copyright 2020 Indev Consultancy Pvt. Ltd.. All rights reserved.
 * Created by : Vimal Kumar
 * Date : 26/10/20 4:39 PM
 * Modified Date :
 * Modifications :
 * Modified By :
 */

package com.sanket.jubifarm.interfaces;

import android.view.View;

import com.sanket.jubifarm.Adapter.VisitPlantAdapter;

public interface ClickListener {
    void onItemClick(int position);
    void onViewLandClick(int position);
    void onEditLandClick(int position);
    void onCheckBoxClick(int position);
}
