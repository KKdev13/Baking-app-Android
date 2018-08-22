package com.example.khale.baking.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.khale.baking.Model.Ingredient;
import com.example.khale.baking.Model.Recipe;
import com.example.khale.baking.R;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        views.removeAllViews(R.id.ingredients_layout);
        views.setTextViewText(R.id.widget_title, recipe.getName());

        for(Ingredient ingredient : recipe.getIngredients()){
            RemoteViews viewsIngredient = new RemoteViews(context.getPackageName(),
                    R.layout.widget_list_item);
            viewsIngredient.setTextViewText(R.id.ingredient_item,
                    String.valueOf(ingredient.getQuantity()) +
                    String.valueOf(ingredient.getMeasure()) + " " + ingredient.getIngredient());
            views.addView(R.id.ingredients_layout, viewsIngredient);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, Recipe recipe){
        for(int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }
}

