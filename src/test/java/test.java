import java.util.concurrent.atomic.AtomicBoolean;

public class test {
    public static void main(String[] args) {
        // 创建一个标志位，用于控制进度条的停止
        AtomicBoolean isRunning = new AtomicBoolean(true);

        // 启动进度条线程
        Thread progressBarThread = new Thread(() -> {
            int progress = 0;
            while (progress <= 100) { // 确保进度条一定会到100%
                System.out.print("\rProgress: [" + repeat("=", progress / 2) + " " + "] " + progress + "%");
                try {
                    Thread.sleep(100); // 模拟进度条更新速度
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                progress++;
            }
            System.out.println(); // 换行
        });

        progressBarThread.start();

        // 模拟一个任务
        try {
            Thread.sleep(5000); // 模拟任务执行时间
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 停止进度条
        isRunning.set(false);

        // 等待进度条线程结束
        try {
            progressBarThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Task completed!");
    }

    // 自定义字符串重复方法
    private static String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
