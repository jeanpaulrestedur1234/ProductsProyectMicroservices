export interface Product{
  id: number;
  name: string;
  sku: string;
  price: number;
  description: string;

}



export interface ProductWithQuantity extends Product {
  quantity: number ;
}
