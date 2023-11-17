package com.my.collector.database

import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class Database {
    companion object {
        private var itemCollections = mutableMapOf<String, MutableList<Item>>()
        private var categoryTargets = mutableMapOf<String, Int>()
        private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("categories")
        private val imgDatabase: StorageReference = FirebaseStorage.getInstance().reference.child("categories")


        fun addItem(name: String, description: String, id: Int, category: String) {
            val item = Item(id,name, description)
            //add item to db
            database.child(category).child(name).setValue(item)

            itemCollections.getOrPut(category) { mutableListOf() }.add(item)
        }

        fun deleteItem(category: String, itemName: String) {
            itemCollections[category]?.removeAll { it.name == itemName }
        }

        fun initCategories(){
            itemCollections = mutableMapOf<String, MutableList<Item>>()
            categoryTargets = mutableMapOf<String, Int>()
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoriesList = mutableListOf<Item>()
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children){
                            //Add category to list
                            addCategory(userSnapshot.key.toString(), userSnapshot.child("target").value.toString().toInt())
                            userSnapshot.children.forEach {
                                if (it.hasChildren()){
                                    val v = it.getValue(Item::class.java)
                                    //add item to category
                                    val item = v?.let { it1 -> Item(it1.id,v.name,v.description) }
                                    if (item != null) {
                                        itemCollections.getOrPut(userSnapshot.key.toString()) { mutableListOf() }.add(item)
                                    }
                                }
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled
                }
            })
        }

        fun getCategories(): List<String> {
            return itemCollections.keys.toList()
        }

        fun addCategory(category: String, target: Int) {
            if (category !in itemCollections) {
                itemCollections[category] = mutableListOf()
                categoryTargets[category] = target
            }
            //Add category to db
            database.child(category).child("target").setValue(target)
        }

        fun targetProgress(category: String): Int {

            var target = categoryTargets[category]
            var itemCount = itemCollections[category]?.size ?: 0
            var difference = target?.minus(itemCount) ?: 0

            //Fetch target for category from db
            database.child(category).get().addOnSuccessListener {
                if (it.exists()){
                    val target_ = it.child("target").value
                    val itemCount = itemCollections[category]?.size ?: 0
                    val difference = target?.minus(itemCount) ?: 0
                }else{

                }
            }.addOnFailureListener{

            }

            return if (difference > 0) (100 * difference / target!!).coerceAtMost(100) else 0
        }

        fun getStatus(): String {
            val count = itemCollections.values.sumOf { it.size }
            return when {
                count < 3 -> "Starter"
                count <= 9 -> "Collector"
                else -> "Packrat"
            }
        }

        fun getItemNamesInCategory(category: String): List<String> =
            itemCollections[category]?.map { it.name } ?: emptyList()

        fun getItemDetails(category: String, itemName: String): Item? =
            itemCollections[category]?.find { it.name == itemName }

        data class Item(
            val id: Int,
            val name: String,
            val description: String,
        )

    }
}