package com.example.qldsv

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qldsv.ui.theme.QLDSVTheme
import com.example.qldsv.ui.theme.Student
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.toUpperCase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QLDSVTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    mainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun mainScreen(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    val context= LocalContext.current
    val jsonData= readFromJson(context,"students.json")
    val listType = object : TypeToken<List<Student>>(){}.type
    val studentList: List<Student> = Gson().fromJson(jsonData,listType)

    Column (
        modifier=modifier.fillMaxWidth().padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,


    ){  Text("Quản lí điểm sinh viên")
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Mã sinh viên") },

            modifier=modifier.fillMaxWidth()
            )
        Text(resultText)
        Button(onClick ={
            resultText=findStudentById(searchText.toUpperCase(),studentList)
        }
        ) {
            Text("Tìm kiếm")
        }
        Column(verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Row(modifier=modifier){
                Text("Mã sinh viên",modifier=modifier.weight(2f))
                Text("Tên sinh viên",modifier=modifier.weight(3f))
                Text("Điểm Toán",modifier=modifier.weight(2f))
                Text("Điểm java",modifier=modifier.weight(2f))
            }
        }
        Divider(color = Color.Black, thickness = 1.dp)
        LazyColumn {
            items(studentList){ student->
                Row(modifier = modifier.fillMaxWidth().padding(vertical = 1.dp)) {
                    Text(student.id,modifier.weight(2f))
                    Text(student.name,modifier.weight(3f))
                    Text(student.math.toString(),modifier.weight(2f))
                    Text(student.java.toString(),modifier.weight(2f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QLDSVTheme {
        mainScreen()
    }
}
fun readFromJson(context: Context, filename: String): String{
    return context.assets.open(filename).bufferedReader().use{ it.readText() }

}
fun findStudentById(id: String,dataList: List<Student>): String{
    val student = dataList.find { it.id == id }
    var result: String
    var average: Double
    var rank: String
    if(student!=null){
        average=(student.math+(student.java-student.math)/2)

        rank=rankStudent(average)

        result = String.format(" Mã sinh viên:%s\n Tên sinh viên: %s\n Điểm trung bình là: %.2f\n Xếp loại: %s", student.id, student.name, average, rank)


    }
    else
        result=("Không tìm thấy sinh viên!")
    return result
}
fun rankStudent(grade:Double): String{

    val result= when {
        grade >= 8.0 -> "Giỏi"
        grade >=6.5 ->"Khá"
        grade >=5 ->"Trung bình"
        else ->"Yếu"
    }
    return result
}
