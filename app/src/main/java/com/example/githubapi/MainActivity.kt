package com.example.githubapi

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapi.model.User
import com.example.githubedwin.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var rvUser: RecyclerView
    private val list = ArrayList<UsersResponse>()
    private val listData = ArrayList<Users>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvUser: RecyclerView = findViewById(R.id.rvUser)
        rvUser.setHasFixedSize(true)
        rvUser.layoutManager = LinearLayoutManager(this)

        getUsers()
//        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Input user name here"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                findUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })
        return true
    }

    private fun findUser(query: String) {
        showLoading(true)
        RetrofitClient.instance.getSearchUser(query).enqueue(
            object: Callback<UserSearchResponse>{
                override fun onResponse(
                    call: Call<UserSearchResponse>,
                    response: Response<UserSearchResponse>
                ) {
                    if (response.isSuccessful) {
                        showLoading(false)
                        val responseBody = response.body()
                        if (responseBody != null) {
                              setViewData(responseBody.items)
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        Toast.makeText(this@MainActivity, "${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                    showLoading(false)
                    Log.e(TAG, "onFailure: ${t.message}")
                }

            }
        )
    }

    private fun setViewData(consumeData: List<ItemsItem>) {
        val listUsers = ArrayList<String>()
        for (dataList in consumeData) {
            val tvImages: ImageView = findViewById(R.id.img_item_avatar)
            val ava = "${dataList.avatarUrl}"
            val imgData = Glide.with(this)
                .load(ava)
                .circleCrop()
                .into(tvImages)

            listUsers.add(
                """
                  ${dataList.login} - ${dataList.type}
                """.trimIndent()
            )
        }
        val adapter = UserSearchAdapter(listUsers)
        val rvUser: RecyclerView = findViewById(R.id.rvUser)
        rvUser.adapter = adapter
    }

    private fun getUsers() {
        showLoading(true)
        RetrofitClient.instance.getListUsers().enqueue(
            object: Callback<ArrayList<UsersResponse>>{
                override fun onResponse(
                    call: Call<ArrayList<UsersResponse>>,
                    response: Response<ArrayList<UsersResponse>>
                ) {
                    if (response.isSuccessful) {
                        showLoading(false)
                        response.body()?.let { list.addAll(it) }
                        val adapter = UserAdapter(list)
                        val rvUser: RecyclerView = findViewById(R.id.rvUser)
                        rvUser.adapter = adapter
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        Toast.makeText(this@MainActivity, "${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ArrayList<UsersResponse>>, t: Throwable) {
                    showLoading(false)
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            }
        )}

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvUser.layoutManager = LinearLayoutManager(this)
        }
        val listUser = ArrayList<UsersResponse>()
        val listUserAdapter = UserAdapter(listUser)
        rvUser.adapter = listUserAdapter
        listUserAdapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: Users) {
        Toast.makeText(this, "Anda memilih " + user.userName, Toast.LENGTH_SHORT).show()
        val moveWithObjectIntent = Intent(this@MainActivity, MoveWithObjectActivity::class.java)
        moveWithObjectIntent.putExtra(MoveWithObjectActivity.EXTRA_USER, user)
        startActivity(moveWithObjectIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        val binding : ProgressBar = findViewById(R.id.progressBar)
        if (isLoading) {
            binding.visibility = View.VISIBLE
        } else {
            binding.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}