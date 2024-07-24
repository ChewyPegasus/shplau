import React, { Component } from 'react'
import { MdOutlineDeleteForever } from "react-icons/md";

export class Order extends Component {
  render() {
    return (
      <div className='item'>
        <img src={"./img/" + this.props.item.img} alt='' />
        <h2>{this.props.item.title}</h2>
        <b>{this.props.item.price}BYN</b>
        <MdOutlineDeleteForever className='delete_icon' size={30} onClick={() => this.props.onDelete(this.props.item.id)}/>
      </div>
    )
  }
}

export default Order