export interface ProductBase{
  name: string;
  sku: string;
  price: number;
  description: string;

}


export interface Product  extends ProductBase {
  id: number;

}

export interface ProductWithQuantity extends ProductBase {
  productId: number ;
  quantity: number ;
}
