package com.codeleg.cashflow.ui.activitiy

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.codeleg.cashflow.R
import com.codeleg.cashflow.databinding.ActivityMainBinding
import com.codeleg.cashflow.ui.fragment.AddFragment
import com.codeleg.cashflow.ui.fragment.EditFragment
import com.codeleg.cashflow.ui.fragment.HomeFragment
import com.codeleg.cashflow.ui.fragment.NavigationListener
import com.codeleg.cashflow.viewmodel.MainViewModel
import kotlin.math.exp

class MainActivity : AppCompatActivity() , NavigationListener {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var mainContainer: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainContainer = binding.mainContainer

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.main_container, HomeFragment())
            }
        }
    }

    override fun navigateToAddExpense() {
        supportFragmentManager.commit {
            replace(R.id.main_container, AddFragment())
            addToBackStack(null)
        }
    }

    override fun navigateToHome() {
        supportFragmentManager.popBackStack()
    }

    override fun navigateToEditExpense(expenseId: Int) {
        supportFragmentManager.commit {
            replace(R.id.main_container, EditFragment.newInstance(expenseId))
            addToBackStack(null)
        }
    }
}