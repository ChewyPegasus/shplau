import React, { Component } from 'react'
import { MdOutlineDeleteForever } from "react-icons/md";
import * as Quantifier from "./Quantifier/Quantifier.tsx"

export class Order extends Component {
  render() {
    return (
      <div className='item'>
        <img src={"./img/" + this.props.item.img} alt='' />
        <h2>{this.props.item.title}</h2>
        <b>{this.props.item.price}BYN</b>
        <Quantifier
              removeProductCallback={() => handleRemoveProduct(product.id)}
              productId={product.id}
              handleUpdateQuantity={handleUpdateQuantity} />
        <MdOutlineDeleteForever className='delete_icon' size={30} onClick={() => this.props.onDelete(this.props.item.id)}/>
      </div>
    )
  }
}

export default Order