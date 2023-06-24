package com.nosh.cheers

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nosh.cheers.data.AppDatabase
import com.nosh.cheers.data.TaskModel
import com.nosh.cheers.ui.theme.CheersTheme
import kotlin.random.Random


class MainActivity : ComponentActivity() {

    private val taskList = arrayListOf<TaskModel>()
    private val db by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheersTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TasksColum(taskList)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getTasksList()
    }

    private fun getTasksList() {
        db.taskDao().getTasks().observe(this) {
            if (!it.isNullOrEmpty()) {
                taskList.clear()
                taskList.addAll(it)
            } else {
                taskList.clear()
            }
        }
    }

}


@Composable
fun TasksColum(tasksList: List<TaskModel>) {
    val contextForToast = LocalContext.current.applicationContext

    Column(horizontalAlignment = Alignment.Start) {
        tasksList.forEach { task ->
            Task(task, contextForToast)
        }
    }
}

@Composable
fun Task(task: TaskModel, contextForToast: Context) {
    var checked by remember {
        mutableStateOf(task.isFinished == 1)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = { newCheckState ->
                checked = newCheckState
                task.isFinished = if (checked) 1 else 0
                Toast.makeText(contextForToast, "${task.title} $newCheckState", Toast.LENGTH_SHORT)
                    .show()
            }
        )

        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = task.title
        )
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
)
@Preview(
    showBackground = true,
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewTaskComponent() {
    val dummyTasks = mutableListOf<TaskModel>()
    val listSize = Random.nextInt(3, 5)..Random.nextInt(7, 12)
    for (i in listSize){
        dummyTasks.add(
            TaskModel(
                title = randomStringByKotlinRandom(Random.nextInt( 3, 12)),
                description = randomStringByKotlinRandom(Random.nextInt( 13, 28)),
                category = randomStringByKotlinRandom(Random.nextInt( 3, 5)),
                isFinished = Random.nextInt(0 , 1),
            )
        )
    }

    CheersTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            TasksColum(dummyTasks)
        }
    }
}

fun randomStringByKotlinRandom(length: Int): String {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('א'..'ת')
    return (1..length).map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")
}

