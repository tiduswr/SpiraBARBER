class AutoComplete{
    constructor(inputElement, listElement, config) {
        //Create default fields
        this.inputFind = document.getElementById(inputElement);
        this.inputList = document.getElementById(listElement);
        this.createElement = (data, inputList) => {
            let keyword = this.inputFind.value;

            if(keyword != ''){
                data.forEach(element => {
                    var li = this.actions.renderElementList(element);
                    li.addEventListener('click', (event) => {
                        this.actions.onSelect(element);
                    });
                    inputList.appendChild(li);
                })
            }
        }

        //create default functions
        let defaultActions = {
            src : [],
            serverSideFilter : false,
            limit : 8,
            filterList : (list, keyword) =>{
                list = list.filter((e) =>{
                    return e.toLowerCase().includes(keyword.toLowerCase());
                });
                return list;
            },

            filterLimit : (list, limit) =>{
                list = list.slice(0, limit-1);
                return list;
            },

            renderElementList : (element) =>{
                var li = document.createElement('li');
                li.setAttribute('type', 'button');
                li.className = 'list-group-item list-group-item-action';
                li.textContent = element;
                return li;
            },

            onSelect : (element) =>{
                this.inputFind.value = element;
            },

            clearAutoCompleteList : (inputList) => {
                inputList.innerHTML = '';
            }
        }

        //Merge default function with user functions
        if(typeof config === 'undefined') {
            this.actions = defaultActions;
        }else{
            this.actions = Object.assign({}, defaultActions, config);
        }

        //Events
        this.inputList.style.width = this.inputFind.offsetWidth + "px";
        window.addEventListener('resize', () =>{
            this.inputList.style.width = this.inputFind.offsetWidth + "px";
        });

        this.actions.clearAutoCompleteList(this.inputList);
        document.addEventListener('click', () => {
            this.actions.clearAutoCompleteList(this.inputList);
        })

        this.inputFind.addEventListener('keyup', () =>{
            let keyword = this.inputFind.value;
            if(keyword != ''){
                if(typeof this.actions.src === 'function'){
                    this.actions.src(keyword).then((d) =>{
                        this.actions.clearAutoCompleteList(this.inputList);
                        if(!this.actions.serverSideFilter) {
                            d = this.actions.filterList(d, keyword);
                            d = this.actions.filterLimit(d, this.actions.limit);
                        }
                        this.createElement(d, this.inputList);
                    })
                }else{
                    this.actions.clearAutoCompleteList(this.inputList);
                    let filteredList = this.actions.filterList(this.actions.src, this.keyword);
                    filteredList = this.actions.filterLimit(this.actions.src, this.actions.limit);
                    this.createElement(filteredList, this.inputList)
                }
            }else{
                this.actions.clearAutoCompleteList(this.inputList);
            }
        })
    }
}