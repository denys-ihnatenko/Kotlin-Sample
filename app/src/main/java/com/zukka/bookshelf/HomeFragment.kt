package com.zukka.bookshelf

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.zukka.bookshelf.databinding.FragmentHomeBinding
import com.zukka.bookshelf.login.LoginViewModel

class HomeFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> Log.i(TAG, "Authenticated")
                // If the user is not logged in, they should not be able to set any preferences,
                // so navigate them to the login fragment
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> findNavController().navigate(
                        R.id.mainFragment
                )
                else -> Log.e(
                        TAG, "New $authenticationState state that doesn't require any UI change"
                )
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_menu, menu)
        return
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.logout_menu_item -> {
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logOut() {
        AuthUI.getInstance().signOut(requireContext())
        findNavController().navigate(
                R.id.mainFragment)
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}