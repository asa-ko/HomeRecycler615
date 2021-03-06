package app.shigenawa.ass.homerecycler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_data.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

   private val realm: Realm by lazy {
       Realm.getDefaultInstance()
   }

   val DateFormat  = SimpleDateFormat("yyyy/MM/dd").format(Date())
   // var DateFormat:String="aaa"

    // val dateText=intent.getStringExtra("date")
    //val date1=dateText.toString()
    // val time:Time?=read()
    //   val todayDate: String? = time.timeData


    /*val Time:List<Time> = listOf(

        Time()

        /*  HomeData("6月3日"),
           HomeData("6月2日"),
           HomeData("6月1日"),
           HomeData("5月31"),
           HomeData("5月30"),
           HomeData("5月29"),
           HomeData("5月28"),
           HomeData("5月27"),
           HomeData("5月26"),
           HomeData("5月25"),
           HomeData("5月24"),
           HomeData("5月23")

         */
    )

     */
    /*val HomeData:List<HomeData> = listOf(
            HomeData(DataFormat)
    )
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dateText=intent.getStringExtra("date")

        val time:Time?=read()
       /* if(time!=null){
            DateFormat=time.timeData
        }

        */

        val taskList=readAll()


        if(taskList.isEmpty()){
            create(DateFormat)
        }

       // val adapter=HomeAdapter( this,taskList,true)

        val adapter =
            HomeAdapter(this, taskList, object : HomeAdapter.OnItemClickListener {
                override fun onItemClick(item: Time) {
                    // クリック時の処理
                    Toast.makeText(applicationContext, item.timeData + "を削除しました", Toast.LENGTH_SHORT).show()
                    delete(item.id)
                }
            }, true,
            object : HomeAdapter.OnButtonClickListener{
                override fun Transition(item: Time) {
                    Toast.makeText(applicationContext,item.timeData+"を表示します",Toast.LENGTH_SHORT).show()
                    trans(item.id)
                }
            })

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=adapter

      // adapter.addAll(taskList)

        addButton.setOnClickListener {

            create(DateFormat)

            val registerPage = Intent(this, RegisterActivity::class.java)
            startActivity(registerPage)
            finish()

         /* val task = Time(timeData = DataFormat.toString())
            adapter.addItem(task)

          */

            //    Snackbar.make(addButton, "Content is empty", Snackbar.LENGTH_SHORT).show()

        }

    }

    fun create(content:String){
        realm.executeTransaction {
            val task=it.createObject(Time::class.java,UUID.randomUUID().toString())
            task.timeData=content
        }
    }

    fun readAll(): RealmResults<Time> {
        return realm.where(Time::class.java).findAll().sort("createdAt", Sort.ASCENDING)
    }

    fun update(id: String, content: String) {
        realm.executeTransaction {
            val task = realm.where(Time::class.java).equalTo("id", id).findFirst()
                ?: return@executeTransaction
            task.timeData = content
        }
    }

    fun update(task: Time, content: String) {
        realm.executeTransaction {
            task.timeData = content
        }
    }

    fun delete(id: String) {
        realm.executeTransaction {
            val task = realm.where(Time::class.java).equalTo("id", id).findFirst()
                ?: return@executeTransaction
            task.deleteFromRealm()
        }
    }

    fun delete(task: Time) {
        realm.executeTransaction {
            task.deleteFromRealm()
        }
    }

    fun deleteAll() {
        realm.executeTransaction {
            realm.deleteAll()
        }
    }

    fun read():Time?{
        return realm.where(app.shigenawa.ass.homerecycler.Time::class.java).findFirst()
    }

    fun trans(id:String){
        val DataPage=Intent(this,ShowActivity::class.java)
        startActivity(DataPage)
        finish()
    }
}