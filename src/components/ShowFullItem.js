import React, { Component } from 'react'

export class ShowFullItem extends Component {
  render() {
    return (
      <div className='full_item'>
        <img src={"./img/" + this.props.item.img} alt='' onClick={() => this.props.onShowItem(this.props.item)} />
        <h2>{this.props.item.title}</h2>
        <p>{this.props.item.desc}</p>
        <b>{this.props.item.price}BYN</b>
        <div className='add_to_cart' onClick={() => this.props.onAdd(this.props.item)}>+</div>
      </div>
    )
  }
}

export default ShowFullItem