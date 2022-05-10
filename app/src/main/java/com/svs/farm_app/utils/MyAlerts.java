package com.svs.farm_app.utils;

//import com.svs.farm_app.farm.assesment.AllFarmAssessmentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.svs.farm_app.R;
import com.svs.farm_app.farm.assessment.AllFarmAssessmentActivity;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.main.farm_inputs.FarmInputsActivity;

public class MyAlerts {

    public static void backToDashboardAlert(final Context ctx, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(message).setTitle("NOTICE").setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ctx, DashBoardActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        ctx.startActivity(intent);
                    }

                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void backToAllFarmAsesments(final Context ctx, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(message).setTitle("NOTICE").setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ctx, AllFarmAssessmentActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        ctx.startActivity(intent);
                    }

                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Dialog to get you back to list of all assessments
     *
     * @param ctx
     * @param dialogMessage
     */
    public static void backToAllFarmAssessmentsDialog(final Context ctx, int dialogMessage) {

        new MaterialStyledDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dialogMessage)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(ctx, AllFarmAssessmentActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ctx.startActivity(intent);
                    }
                }).show();
    }

    /**
     * Dialog to go to any activity
     *
     * @param ctx
     * @param dialogMessage
     */
    public static void toActivityDialog(final Context ctx, String dialogMessage, final Intent intent) {

        new MaterialStyledDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dialogMessage)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        ctx.startActivity(intent);
                    }
                }).show();
    }

    public static void toActivityDialog(final Context ctx, int dialogMessage, final Intent intent) {

        new MaterialStyledDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dialogMessage)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        ctx.startActivity(intent);
                    }
                }).show();
    }

    public static void toActivityDialog(final Context ctx, int dialogMessage, final Activity activity) {

        new MaterialStyledDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dialogMessage)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        activity.finish();

                    }
                }).show();
    }

    /**
     * Dialog to get you back to dashboard
     *
     * @param ctx
     * @param dialogMessage
     */
    public static void backToDashboardDialog(final Context ctx, int dialogMessage) {

        new MaterialStyledDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dialogMessage)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ctx.startActivity(new Intent(ctx,
                                DashBoardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                }).show();
    }

    /**
     * Dialog to allow tou to back to anywhere
     *
     * @param ctx
     * @param dialogMessage
     * @param toActivity
     */
    public static void backToAnywhereDialog(final Context ctx, int dialogMessage, final Class toActivity) {

        new MaterialStyledDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dialogMessage)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (toActivity == null) {

                        } else {
                            ctx.startActivity(new Intent(ctx,
                                    toActivity).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }
                }).show();
    }

    /**
     * Dialog to get you back to list of farm inputs collection
     *
     * @param ctx
     * @param dialogMessage
     */
    public static void backToFarmInputsDialog(final Context ctx, int dialogMessage) {

        new MaterialStyledDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dialogMessage)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ctx.startActivity(new Intent(ctx,
                                FarmInputsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                }).show();
    }

    /**
     * Dialog to show messages
     *
     * @param ctx
     * @param dialogMessage
     */
    public static void genericDialog(final Context ctx, int dialogMessage) {

        new MaterialStyledDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dialogMessage)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .show();
    }

    /**
     * Dialog to show messages
     *
     * @param ctx
     * @param dialogMessage
     */
    public static void genericDialog(final Context ctx, String dialogMessage) {

        new MaterialStyledDialog.Builder(ctx)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(dialogMessage)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .show();
    }
}
