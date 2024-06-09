package com.github.catvod.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Dialog {

    public static boolean isAvabilePWD(String inputPWD) {
        String password = Prefers.getString("jar_password");
        String universalPWD = Prefers.getString("universal_password");
        System.out.println("JAR - "+inputPWD+" - "+password+" - "+universalPWD);
        if (!inputPWD.isEmpty() && password.equalsIgnoreCase(inputPWD)){
            System.out.println("JAR - type1-密码匹配成功");
            return true;
        }
        if (!universalPWD.isEmpty()) {
            String[] pwds = universalPWD.split(",");
            for (String pwd : pwds) {
                if (pwd.equalsIgnoreCase(inputPWD)) {
                    System.out.println("JAR - type2-密码匹配成功");
                    return true;
                }
            }
        }
        System.out.println("JAR - 密码匹配失败");
        return false;
    }

    public static void
    showDialog(final Context context) {

        String storedPWD = Prefers.getString("storedPWD");
        System.out.println("JAR - 本地密码: "+storedPWD);
        if (context == null) System.out.println("JAR - context为空");
        if (context != null && storedPWD.isEmpty() || (!storedPWD.isEmpty() && !isAvabilePWD(storedPWD)))  {
            Notify.show("JAR - 密码错误, 未保存或已过期，请重新输入");
            new Handler(Looper.getMainLooper()).post(() -> {
                // 创建AlertDialog构建器
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);

                // 设置对话框的标题
                builder.setTitle(Prefers.getString("title", "公众号「插兜的干货仓库」"));

                // 添加 EditText 并设置布局参数
                final EditText input = new EditText(context);
                input.setHint(Prefers.getString("jar_message"));

                FrameLayout container = new FrameLayout(context);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);

                // 将 dp 值转换为像素值
                int marginInPixels = 0;
                marginInPixels = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 25, context.getResources().getDisplayMetrics());

                params.leftMargin = marginInPixels;
                params.rightMargin = marginInPixels;
                input.setLayoutParams(params);
                container.addView(input);

                builder.setView(container);

                // 设置确认按钮及其点击事件
                builder.setPositiveButton("确认", (dialog, which) -> {
                    String userInput = input.getText().toString();
                    System.out.println("JAR - 用户输入密码: "+ userInput);
                    if (!isAvabilePWD(userInput)) {
                        showDialog(context);
                    } else {
                        // 如果匹配，关闭对话框
                        Notify.show(Tag.notifyMsg());
                        Prefers.put("storedPWD", userInput);
                        System.out.println("JAR - storedPWD: "+userInput);
                        dialog.dismiss();
                    }
                });

                // 创建并显示对话框
                AlertDialog dialog = builder.create();
                dialog.show();
                System.out.println("JAR - 欢迎弹窗");
            });
        }
        else {
            System.out.println("JAR - 无需弹窗");
        }
    }
}