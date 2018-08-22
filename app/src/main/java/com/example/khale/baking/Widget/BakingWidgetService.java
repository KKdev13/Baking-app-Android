package com.example.khale.baking.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.khale.baking.Model.Recipe;

public class BakingWidgetService extends IntentService {

    public static final String ACTION_RECIPE_UPDATE =
            "com.example.khale.baking.widget.action.recipe_update";
    private static final String BUNDLE_RECIPE_WIDGET_DATA =
            "com.example.khale.baking.widget.widget_data";

    public BakingWidgetService(){
        super("BakingWidgetService");
    }

    public static void startActionRecipeUpdate(Context context, Recipe recipe){
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_RECIPE_UPDATE);
        intent.putExtra(BUNDLE_RECIPE_WIDGET_DATA, recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_RECIPE_UPDATE.equals(action) &&
                    intent.getParcelableExtra(BUNDLE_RECIPE_WIDGET_DATA) != null){
                handleActionRecipeUpdate(intent.<Recipe>getParcelableExtra(BUNDLE_RECIPE_WIDGET_DATA));

            }
        }
    }

    private void handleActionRecipeUpdate(Recipe recipe){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        BakingWidgetProvider.updateBakingWidgets(this, appWidgetManager, appWidgetIds, recipe);
    }
}
