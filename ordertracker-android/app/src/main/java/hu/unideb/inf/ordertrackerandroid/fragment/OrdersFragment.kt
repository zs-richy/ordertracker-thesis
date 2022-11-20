package hu.unideb.inf.ordertrackerandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import hu.unideb.inf.ordertrackerandroid.R
import hu.unideb.inf.ordertrackerandroid.adapter.*
import hu.unideb.inf.ordertrackerandroid.databinding.FragmentOrdersBinding
import hu.unideb.inf.ordertrackerandroid.model.api.Order
import hu.unideb.inf.ordertrackerandroid.util.WidgetUtils
import hu.unideb.inf.ordertrackerandroid.viewmodel.OrdersViewModel

class OrdersFragment: Fragment() {

    val ordersViewModel: OrdersViewModel by viewModels()

    lateinit var binding: FragmentOrdersBinding

    private var adapter: OrderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        binding.swipeRefresh.setColorSchemeColors(WidgetUtils.getColorFromTheme(requireContext(), R.attr.colorPrimary))

        setupAdapter()

        setupObservers()

        setupListeners()

        return binding.root
    }

    private fun setupAdapter() {
        if (adapter == null) {
            adapter = OrderAdapter(requireContext(), object : OrderAdapterListener {
                override fun onClick(order: Order) {
                    val action = OrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment().setOrderId(order.id ?: -1)
                    findNavController().navigate(action)
                }
            })
        }

        binding.rvOrders.adapter = adapter
    }

    private fun setupObservers() {
        ordersViewModel.orders.observe(this) { orders ->
            if (orders != null) {
                adapter?.setItems(orders)
                adapter?.notifyDataSetChanged()
            }
        }

        ordersViewModel.isFetchingInProgress.observe(this, { inProgress ->
            binding.swipeRefresh.isRefreshing = inProgress
        })
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            ordersViewModel.fetchOrders()
        }
    }

    override fun onResume() {
        super.onResume()

        if (ordersViewModel.orders.value.isNullOrEmpty()) {
            ordersViewModel.fetchOrders()
        }
    }

}