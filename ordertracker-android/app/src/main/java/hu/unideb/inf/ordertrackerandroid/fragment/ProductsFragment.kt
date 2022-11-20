package hu.unideb.inf.ordertrackerandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import hu.unideb.inf.ordertrackerandroid.R
import hu.unideb.inf.ordertrackerandroid.adapter.ProductAdapter
import hu.unideb.inf.ordertrackerandroid.adapter.ProductAdapterListener
import hu.unideb.inf.ordertrackerandroid.databinding.FragmentProductsBinding
import hu.unideb.inf.ordertrackerandroid.model.api.Product
import hu.unideb.inf.ordertrackerandroid.util.WidgetUtils
import hu.unideb.inf.ordertrackerandroid.viewmodel.CartViewModel
import hu.unideb.inf.ordertrackerandroid.viewmodel.ProductsViewModel

class ProductsFragment: Fragment() {

    lateinit var binding: FragmentProductsBinding

    val productsViewModel: ProductsViewModel by activityViewModels()

    val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.floatingCartLayout.lifecycleOwner = viewLifecycleOwner

        binding.swipeRefresh.setColorSchemeColors(WidgetUtils.getColorFromTheme(requireContext(), R.attr.colorPrimary))

        val adapter = ProductAdapter(requireContext(), object: ProductAdapterListener {
            override fun onClick(product: Product) {
                cartViewModel.addItemToCart(product)
            }
        })
        binding.rvProducts.adapter = adapter

        productsViewModel.products.observe(this, { products ->
            if (products != null) {
                adapter.products = products.productList
                adapter.notifyDataSetChanged()
            }
        })

        productsViewModel.isRefreshing.observe(this, { refreshing ->
            binding.swipeRefresh.isRefreshing = refreshing
        })

        binding.swipeRefresh.setOnRefreshListener {
            productsViewModel.initProducts()
        }

        binding.floatingCartLayout.btnCart.setOnClickListener {
            if (!cartViewModel.cart.value.isNullOrEmpty()) {
                findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToCartFragment())
            }
        }

        binding.floatingCartLayout.cartViewModel = cartViewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (productsViewModel.products.value?.productList.isNullOrEmpty()) {
            productsViewModel.initProducts()
        }
    }

}