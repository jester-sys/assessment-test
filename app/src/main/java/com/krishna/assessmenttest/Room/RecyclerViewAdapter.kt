package com.krishna.assessmenttest.Room

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.krishna.assessmenttest.R
import com.krishna.assessmenttest.databinding.ItemUserDataBinding
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(private val cxt: Context) : RecyclerView.Adapter<RecyclerViewAdapter.RoomViewHolder>() {
    private val allList: ArrayList<Model> = ArrayList()

    inner class RoomViewHolder(val binding: ItemUserDataBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = ItemUserDataBinding.inflate(LayoutInflater.from(cxt), parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val list = allList[position]

        holder.binding.tvName.text = list.name
        holder.binding.tvPhoneNumber.text = list.phoneNumber
        holder.binding.tvDateOfBirth.text = list.dob
        holder.binding.tvEmail.text = list.email

        list.photoProfile.let { uri ->
            Picasso.get().load(uri).placeholder(R.drawable.ic_placeholder)
                .into(holder.binding.ivProfilePic)
        } ?: run {
            Picasso.get().load(R.drawable.ic_placeholder).into(holder.binding.ivProfilePic)
        }
    }
    override fun getItemCount(): Int {
        return allList.size
    }

    // Method to set new data and update the RecyclerView
    fun setData(newData: List<Model>) {
        allList.clear()
        allList.addAll(newData)
        notifyDataSetChanged()
    }
}

