import React, { Component } from 'react'

export class Categories extends Component {
    constructor(props) {
        super(props)
        this.state = {
            categories: [
                {
                    key: 'all',
                    name: 'Все'
                },
                {
                    key: 'stuff',
                    name: 'Оборудование'
                },
                {
                    key: 'rent',
                    name: 'Аренда'
                },
                {
                    key: 'work',
                    name: 'Услуги на сплаве'
                },
                {
                    key: 'food',
                    name: 'Еда'
                }
            ]
        }
    }
  render() {
    return (
      <div className='categories'>
        {this.state.categories.map(el => (
            <div key={el.key} onClick={() => this.props.chooseCategory(el.key)}>{el.name}</div>
        ))}
      </div>
    )
  }
}

export default Categories