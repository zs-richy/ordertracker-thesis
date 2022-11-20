package hu.unideb.inf.ordertrackerandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import hu.unideb.inf.ordertrackerandroid.R
import hu.unideb.inf.ordertrackerandroid.databinding.FragmentOrderDetailsBinding
import hu.unideb.inf.ordertrackerandroid.util.WidgetUtils
import hu.unideb.inf.ordertrackerandroid.viewmodel.OrderDetailsViewModel

class OrderDetailsFragment: Fragment() {

    val args: OrderDetailsFragmentArgs by navArgs()

    val orderDetailsViewModel: OrderDetailsViewModel by viewModels()

    lateinit var binding: FragmentOrderDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.orderDetailsViewModel = orderDetailsViewModel

        binding.swipeRefresh.setColorSchemeColors(WidgetUtils.getColorFromTheme(requireContext(), R.attr.colorPrimary))

        orderDetailsViewModel.initByOrderId(args.orderId)

        setupObservers()

        setupListeners()

        return binding.root
    }

    private fun setupObservers() {
        orderDetailsViewModel.isFetchingOrderData.observe(this) { isFetching ->
            binding.swipeRefresh.isRefreshing = isFetching
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            orderDetailsViewModel.fetchOrderDetails()
        }
    }

}