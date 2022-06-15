package com.example.githubapi

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubedwin.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var rvUser: RecyclerView
    private val list = ArrayList<UsersResponse>()
    private val listData = ArrayList<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUser = findViewById(R.id.rvUser)
        rvUser.setHasFixedSize(true)

        rvUser.layoutManager = LinearLayoutManager(this)

        getUsers()
        showRecyclerList()
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
        val adapter = UserSearchAdapter(consumeData)
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

    private fun getDetailUser(consumeData: UsersResponse) {
        showLoading(true)
        RetrofitClient.instance.getDetailUser(consumeData.login).enqueue(
            object: Callback<List<UsersResponse>> {
                override fun onResponse(
                    call: Call<List<UsersResponse>>,
                    response: Response<List<UsersResponse>>
                ) {
                    if (response.isSuccessful) {
                        showLoading(false)
                        response.body()?.let { list.addAll(it) }
                        val adapter = UserAdapter(list)
                        val rvUser: RecyclerView = findViewById(R.id.rvUser)
                        rvUser.adapter = adapter
                        Log.e(TAG, "Response sukses")
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        Toast.makeText(this@MainActivity, "${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<UsersResponse>>, t: Throwable) {
                  showLoading(false)
                    Log.e(TAG, "onFailure: ${t.message}")
                }

            }
        )
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvUser.layoutManager = LinearLayoutManager(this)
        }
        val userAdapter = UserAdapter(list)
        rvUser.adapter = userAdapter
        userAdapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersResponse) {
                showSelectedUser(data)
                Log.d(TAG, data.login)
            }
        })
    }

    private fun showSelectedUser(consumeData: UsersResponse) {
        Toast.makeText(this, "Anda memilih " +  consumeData.login, Toast.LENGTH_SHORT).show()
        getDetailUser(consumeData)
        val moveWithObjectIntent = Intent(this@MainActivity, MoveWithObjectActivity::class.java)
        moveWithObjectIntent.putExtra(MoveWithObjectActivity.EXTRA_USER, list)
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