package com.example.videoplayertutorial.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.coil.CoilIntegration
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

interface Supabase {

        val supabase:SupabaseClient

        val videoBucket:BucketApi

}

object client: Supabase{
    override val supabase = createSupabaseClient(
    supabaseUrl = "https://sbrxdfhahdxjjybpwlco.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNicnhkZmhhaGR4amp5YnB3bGNvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTQ5NzU0NjgsImV4cCI6MjAzMDU1MTQ2OH0.OGns8NTxIhgUA5i-sopybWWUDVbnTKMcNznVhrIOrcM"
    ) {
        install(Postgrest)
        install(Storage)
        install(CoilIntegration)
    }

    override val videoBucket = supabase.storage.from("videos")

}