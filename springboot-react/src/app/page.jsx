"use client";

import { useState, useEffect } from "react";

export default function Home() {
  const [products, setProducts] = useState([]);
  const [name, setName] = useState("");
  const [value, setValue] = useState("");
  const [imageUrl, setImageUrl] = useState("");

  useEffect(() => {
    console.log("Componente montado");
    getApi();
  }, []);

  async function getApi() {
    const response = await fetch("http://localhost:8080/products");
    const data = await response.json();
    console.log(data);
    setProducts(data);
  }

  async function addProduct() {
    const response = await fetch("http://localhost:8080/products", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name,
        value,
        imageUrl,
      }),
    });
    const data = await response.json();
    console.log(data);
    setTimeout(() => {
      getApi();
    }, 1000);
  }

  async function deleteProduct(id) {
    const response = await fetch(`http://localhost:8080/products/${id}`, {
      method: "DELETE",
    });
    setTimeout(() => {
      getApi();
    }, 1000);
  }

  return (
    <main className="flex flex-col items-center bg-slate-100 h-screen">
      <header className="flex justify-center items-center">
        <h1 className="text-2xl">SpringShop</h1>
      </header>
      <div className="flex gap-2 mt-4">
        {products.map((product) => (
          <div
            key={product.id}
            className="flex flex-col justify-center items-center shadow-lg rounded-lg bg-white p-4"
          >
            <div className="flex justify-end w-full">
              <button
                onClick={(e) => {
                  deleteProduct(product.id);
                }}
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="m9.75 9.75 4.5 4.5m0-4.5-4.5 4.5M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"
                  />
                </svg>
              </button>
            </div>
            <div className="flex flex-col justify-center items-center">
              <img
                src={product.imageUrl}
                className="object-contain h-48 w-96"
              />
              <h2>{product.name}</h2>
              <p>Preço: {product.value}</p>
              <button className="text-white p-4 rounded-md transition ease-in-out delay-150 bg-blue-500 hover:-translate-y-1 hover:scale-110 hover:bg-indigo-500 duration-300 mt-4 w-32">
                Comprar
              </button>
            </div>
          </div>
        ))}
      </div>
      <p className="text-lg mt-4">Adicionar Produto</p>
      <div className="flex gap-4 items-center">
        <input
          type="text"
          placeholder="Produto:"
          className="h-10 rounded-md border border-black p-4"
          onChange={(e) => setName(e.target.value)}
        />
        <input
          type="text"
          placeholder="Preço:"
          className="h-10 rounded-md border border-black p-4"
          onChange={(e) => setValue(e.target.value)}
        />
        <input
          type="text"
          placeholder="Imagem:"
          className="h-10 rounded-md border border-black p-4"
          onChange={(e) => setImageUrl(e.target.value)}
        />
        <button
          className="text-white p-4 rounded-md transition ease-in-out delay-150 bg-blue-500 hover:-translate-y-1 hover:scale-110 hover:bg-indigo-500 duration-300 w-32"
          onClick={addProduct}
        >
          Adicionar
        </button>
      </div>
    </main>
  );
}
