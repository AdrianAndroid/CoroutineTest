package com.example.coroutinetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.runBtn).setOnClickListener {
//            testRunBlocking()
//            testRunLaunch()
//            testRunWithContext()
            testAsync()
        }
    }

    private fun run5() {
        //1、不阻塞主线程（推荐）
        CoroutineScope(Dispatchers.IO).launch {
            //执行代码.....
        }

        //2、塞主线程（推荐）
        GlobalScope.launch(Dispatchers.IO) {
            //执行代码.....
        }


        //3、优秀的线程切换
        CoroutineScope(Dispatchers.Main).launch {
            val task1 = withContext(Dispatchers.IO) {
                currentTime()
                delay(2000)
                "服务器返回值：json"  //服务器返回结果赋值给task1
            }
            //刷新UI，task1
            currentTime()
            Log.d("LUO", "值===========${task1}")
        }
        currentTime()
    }


    private fun testAsync(){
        CoroutineScope(Dispatchers.Main).launch {
            val time1 = System.currentTimeMillis()

            val task1 = async(Dispatchers.IO) {
                delay(2000)
                Log.e("LUO", "1.执行task1.... [当前线程为：${Thread.currentThread().name}]")
                "one"  //返回结果赋值给task1
            }

            val task2 = async(Dispatchers.IO) {
                delay(1000)
                Log.e("LUO", "2.执行task2.... [当前线程为：${Thread.currentThread().name}]")
                "two"  //返回结果赋值给task2
            }

            Log.e("LUO", "task1 = ${task1.await()}  , task2 = ${task2.await()} , 耗时 ${System.currentTimeMillis() - time1} ms  [当前线程为：${Thread.currentThread().name}]")
        }
    }

    private fun testRunWithContext() {
        CoroutineScope(Dispatchers.Main).launch {
            val time1 = System.currentTimeMillis()

            val task1 = withContext(Dispatchers.IO) {
                delay(2000)
                Log.e("LUO", "1.执行task1.... [当前线程为：${Thread.currentThread().name}]")
                "one"  //返回结果赋值给task1
            }

            val task2 = withContext(Dispatchers.IO) {
                delay(1000)
                Log.e("LUO", "2.执行task2.... [当前线程为：${Thread.currentThread().name}]")
                "two"  //返回结果赋值给task2
            }
            Log.e("LUO", "task1 = $task1  , task2 = $task2 , 耗时 ${System.currentTimeMillis()-time1} ms  [当前线程为：${Thread.currentThread().name}]")
        }
    }


    private fun testRunLaunch() {
        //GlobalScope主协程
        GlobalScope.launch {
            Log.d("LUO", "1 主协程1===========${Thread.currentThread().name}")
        }
        GlobalScope.launch {
            Log.d("LUO", "2 主协程1===========${Thread.currentThread().name}")
        }
        GlobalScope.launch {
            Log.d("LUO", "3 主协程1===========${Thread.currentThread().name}")
        }
        Thread(Runnable {
            GlobalScope.launch {
                Log.d("LUO", "Thread1 主协程1===========${Thread.currentThread().name}")
            }
        }).start()
        Thread(Runnable {
            GlobalScope.launch {
                Log.d("LUO", "Thread2 主协程1===========${Thread.currentThread().name}")
            }
        }).start()
        Thread(Runnable {
            GlobalScope.launch {
                Log.d("LUO", "Thread3 主协程1===========${Thread.currentThread().name}")
            }
        }).start()

        //GlobalScope主协程,main线程
        GlobalScope.launch(Dispatchers.Main) {
            Log.d("LUO", "主协程2===========${Thread.currentThread().name}")
        }

        //GlobalScope主协程,IO线程
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("LUO", "主协程3===========${Thread.currentThread().name}")
        }

        //runBlocking主协程
        runBlocking {
            Log.d("LUO", "runBlocking start")
            launch {
                Log.d("LUO", "runBlocking in launch")
            }
            Log.d("LUO", "runBlocking end")
        }
        //启动主协程
        GlobalScope.async {
            Log.d("LUO", "主协程4===========${Thread.currentThread().name}")
        }
        //自定义协程
        val scope = CoroutineScope(EmptyCoroutineContext)
        scope.launch {
            Log.d("LUO", "1 CoroutineScope EmptyCoroutineContext launch ${Thread.currentThread().name}")
        }
        scope.async {
            Log.d("LUO", "1 CoroutineScope EmptyCoroutineContext async ${Thread.currentThread().name}")
        }

        val scope2 = CoroutineScope(EmptyCoroutineContext)
        scope2.launch {
            Log.d("LUO", "2 1 CoroutineScope EmptyCoroutineContext launch ${Thread.currentThread().name}")
        }
        scope2.launch {
            Log.d("LUO", "2 2 CoroutineScope EmptyCoroutineContext launch ${Thread.currentThread().name}")
        }
        scope2.launch {
            Log.d("LUO", "2 3 CoroutineScope EmptyCoroutineContext launch ${Thread.currentThread().name}")
        }
        scope2.launch {
            Log.d("LUO", "2 4 CoroutineScope EmptyCoroutineContext launch ${Thread.currentThread().name}")
        }
        scope2.async {
            Log.d("LUO", "2 CoroutineScope EmptyCoroutineContext async ${Thread.currentThread().name}")
        }
    }

    private fun testRunBlocking() {
        currentTime()
        //调用协程方法
        run1()
        currentTime()
    }

    private fun currentTime() {
        Log.d("LUO",SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date()))
    }

    //所有的协程类型
    private fun run1() {
        //默认主协程
        runBlocking {
            Log.d("LUO", "主协程1===========${Thread.currentThread().name}")
        }
        //main主协程
        currentTime()
//        runBlocking(Dispatchers.Main) {
//            Log.d("LUO", "主协程2===========${Thread.currentThread().name}")
//        }
        currentTime()
        //IO主协程
        runBlocking(Dispatchers.IO) {
            delay(2000)
            Log.d("LUO", "主协程3===========${Thread.currentThread().name}")
        }
        runBlocking {
            Log.d("LUO", "主协程1===========${Thread.currentThread().name}")
        }
        currentTime()
        //runBlocking最后一个就是返回值
        val job = runBlocking {
            "我是小白啊"
        }
//        currentTime()
        Log.d("LUO","job========${job}")

        Thread(Runnable {
            runBlocking {
                Log.d("LUO", "Thread 主协程1===========${Thread.currentThread().name}")
            }
        }).start()
    }
}