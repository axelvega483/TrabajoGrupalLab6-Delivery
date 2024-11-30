package com.example.delivery.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delivery.R;
import com.example.delivery.data.model.Cliente;
import com.example.delivery.data.model.Pedido;
import com.example.delivery.data.model.PedidoDetalle;
import com.example.delivery.data.model.Repartidor;
import com.example.delivery.ui.viewmodel.ClienteViewModel;
import com.example.delivery.ui.viewmodel.PedidoDetalleViewModel;
import com.example.delivery.ui.viewmodel.PedidosViewModel;
import com.example.delivery.ui.viewmodel.RepartidorViewModel;
import com.google.android.gms.maps.MapView;

import java.util.concurrent.atomic.AtomicReference;

public class PedidoAceptadoFragment extends Fragment {
    private EditText edCodigoEntrega;
    private TextView tvNroPedido, tvNombreCliente, tvTotal;
    private Button btnConfirmarEntrega, btnVolver;
    private MapView mapViewPedido;
    private RepartidorViewModel repartidorViewModel;
    private PedidoDetalleViewModel pedidoDetalleViewModel;
    private ClienteViewModel clienteViewModel;
    private PedidosViewModel pedidosViewModel;
    private Long pedidoId;


    public static PedidoAceptadoFragment newInstance() {
        Bundle args = new Bundle();
        PedidoAceptadoFragment fragment = new PedidoAceptadoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("PEDIDOACEPTADO-ONCREATE", "CREANDO FRAGMENTO");

        // Asegúrate de inicializar el ViewModel aquí
        pedidosViewModel = new ViewModelProvider(requireActivity()).get(PedidosViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pedido_aceptado, container, false);

        // Solo inicializamos si no se ha hecho ya
        init(root);
        initListener();
        Log.d("PedidoAceptadoFragment", "Pedido ID: " + pedidoId);
        setearDatos();

        Log.e("PEDIDOACEPTADO-ONCREATEview", "CREANDO FRAGMENTO");
        return root;
    }


    private void setearDatos() {
        if (getArguments() != null) {
            pedidoId = getArguments().getLong("pedidoId");
            Log.e("PEDDDDDIDO", pedidoId.toString());
            pedidosViewModel.findById(pedidoId).observe(requireActivity(), pedido -> {
                Log.e("asdasdasd", pedidoId.toString());
                pedidosViewModel.setPedido(pedido);
            });
        }
        Log.e("llega el pedido?", pedidoId.toString());
        pedidosViewModel.findById(pedidoId).observe(requireActivity(), pedido -> {
            tvNroPedido.setText(pedido.getId().toString());
            clienteViewModel.findById(pedido.getClienteId()).observe(getViewLifecycleOwner(), cliente -> {
                tvNombreCliente.setText(cliente.getNombre() + " " + cliente.getApellido());
            });
        });
    }


    private void initListener() {
        btnConfirmarEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Entrega Confirmada", Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, PedidosFragment.newInstance()).commit();
            }
        });
    }

    private void init(View root) {
        repartidorViewModel = new ViewModelProvider(this).get(RepartidorViewModel.class);
        pedidoDetalleViewModel = new ViewModelProvider(this).get(PedidoDetalleViewModel.class);
        clienteViewModel = new ViewModelProvider(this).get(ClienteViewModel.class);
        edCodigoEntrega = root.findViewById(R.id.edCodigoEntrega);
        btnConfirmarEntrega = root.findViewById(R.id.btnConfirmarEntrega);
        btnVolver = root.findViewById(R.id.btnVolver);
        mapViewPedido = root.findViewById(R.id.mapViewPedido);
        tvNombreCliente = root.findViewById(R.id.tvNombreCliente);
        tvNroPedido = root.findViewById(R.id.tvNroPedido);
        tvTotal = root.findViewById(R.id.tvTotal);
    }
}